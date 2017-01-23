/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.mysensors;

import com.whizzosoftware.hobson.api.device.HobsonDeviceDescriptor;
import com.whizzosoftware.hobson.api.device.proxy.HobsonDeviceProxy;
import com.whizzosoftware.hobson.api.plugin.channel.AbstractChannelObjectPlugin;
import com.whizzosoftware.hobson.api.plugin.channel.ChannelObjectDriverInboundHandler;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.mysensors.protocol.*;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Hobson plugin that can manage a MySensors gateway.
 *
 * @author Dan Noguerol
 */
public class MySensorsPlugin extends AbstractChannelObjectPlugin implements MessageProcessorDelegate, MessageSender {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String NODE_ID_SEPERATOR = ".";
    private static final String PROP_SENSOR_SYSTEM = "system";

    private MySensorsMessageProcessor processor = new MySensorsMessageProcessor();
    private Map<Integer,MySensorsNodeInfo> nodeInfoMap = new HashMap<>();
    private String sensorSystem;
    private MessageSender messageSender;
    private List<String> assignableNodeIds = new ArrayList<>();

    public MySensorsPlugin(String pluginId, String version, String description) {
        super(pluginId, version, description);
        messageSender = this;

        // start with all node IDs being assignable
        for (int i=1; i < 255; i++) {
            assignableNodeIds.add(Integer.toString(i));
        }
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void onStartup(PropertyContainer config) {
        super.onStartup(config);

        // if sensor system isn't set, default to imperial
        sensorSystem = config != null ? config.getStringPropertyValue(PROP_SENSOR_SYSTEM) : null;
        if (sensorSystem == null) {
            sensorSystem = "I";
            setPluginConfigurationProperty(getContext(), PROP_SENSOR_SYSTEM, sensorSystem);
        }

        // publish any previously published sensors
        for (HobsonDeviceDescriptor hdd: getPublishedDeviceDescriptions()) {
            publishDeviceProxy(new MySensorsSensor(this, hdd));
            String id = hdd.getContext().getDeviceId();
            if (id.contains(".")) {
                id = id.substring(0, id.indexOf("."));
            }
            assignableNodeIds.remove(id);
        }

        logger.debug("Current assignable node ID count: {}", assignableNodeIds.size());
    }

    @Override
    protected TypedProperty[] getConfigurationPropertyTypes() {
        return new TypedProperty[] {
            new TypedProperty.Builder(PROP_SERIAL_PORT, "Serial Port", "The serial port for the MySensors gateway", TypedProperty.Type.SERIAL_PORT).build(),
            new TypedProperty.Builder(PROP_SENSOR_SYSTEM, "System", "The system of measure sensors should use", TypedProperty.Type.STRING).
                enumerate("I", "Imperial").
                enumerate("M", "Metric").
                build()
        };
    }

    @Override
    public void onPluginConfigurationUpdate(PropertyContainer config) {
        super.onPluginConfigurationUpdate(config);
        if (config.hasPropertyValue(PROP_SENSOR_SYSTEM)) {
            sensorSystem = config.getStringPropertyValue(PROP_SENSOR_SYSTEM);
        }
    }

    @Override
    public String getName() {
        return "MySensors";
    }

    /**
     * Configures the Netty channel pipeline. This can be overridden by subclasses if needed.
     *
     * @param pipeline the current channel pipeline
     */
    @Override
    protected void configurePipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new LineBasedFrameDecoder(256, true, true));
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("client", new ChannelObjectDriverInboundHandler(this));
    }

    @Override
    protected ChannelInboundHandlerAdapter getDecoder() {
        return new MySensorsDecoder();
    }

    @Override
    protected ChannelOutboundHandlerAdapter getEncoder() {
        return new MySensorsEncoder();
    }

    @Override
    protected void configureChannel(ChannelConfig cfg) {
        if (cfg instanceof RxtxChannelConfig) {
            RxtxChannelConfig config = (RxtxChannelConfig)cfg;
            config.setBaudrate(115200);
            config.setDatabits(RxtxChannelConfig.Databits.DATABITS_8);
            config.setStopbits(RxtxChannelConfig.Stopbits.STOPBITS_1);
            config.setParitybit(RxtxChannelConfig.Paritybit.NONE);
        }
    }

    @Override
    protected void onChannelConnected() {
    }

    @Override
    protected void onChannelData(Object o) {
        processor.process(this, (MySensorsMessage)o);
    }

    @Override
    protected void onChannelDisconnected() {

    }

    @Override
    public void sendMessage(MySensorsMessage msg) {
        messageSender.doSendMessage(msg);
    }

    @Override
    public void doSendMessage(MySensorsMessage msg) {
        send(msg);
    }

    @Override
    public void onInternalMessage(int nodeId, int childSensorId, InternalType type, String payload) {
        removeAssignableNodeId(nodeId);

        switch (type) {
            case I_BATTERY_LEVEL:
                int percent = Integer.parseInt(payload);
                logger.trace("Received node battery update: {}={}", nodeId, percent);
                String id = Integer.toString(nodeId);
                for (HobsonDeviceProxy proxy : getDeviceProxies()) {
                    if (proxy.getContext().getDeviceId().startsWith(id + ".")) {
                        ((MySensorsSensor)proxy).onBatteryUpdate(percent);
                    }
                }
                break;
            case I_CONFIG:
                logger.debug("Received config request; sending response");
                sendMessage(new MySensorsMessage(nodeId, childSensorId, CommandType.INTERNAL, false, InternalType.I_CONFIG, sensorSystem));
                break;
            case I_DEBUG:
                logger.debug(payload);
                break;
            case I_GATEWAY_READY:
                logger.debug("Gateway has reported ready");
                break;
            case I_HEARTBEAT_RESPONSE:
                logger.debug("Got heartbeat from node: {}", nodeId);
                long now = System.currentTimeMillis();
                for (HobsonDeviceProxy proxy : getDeviceProxies()) {
                    if (proxy.getContext().getDeviceId().startsWith(nodeId + NODE_ID_SEPERATOR)) {
                        proxy.setLastCheckin(now);
                    }
                }
                break;
            case I_ID_REQUEST:
                String newNodeId = getNextNodeId();
                if (newNodeId != null) {
                    sendMessage(new MySensorsMessage(nodeId, childSensorId, CommandType.INTERNAL, false, InternalType.I_ID_RESPONSE, newNodeId));
                } else {
                    logger.error("Unable to assign ID to new node - maximum number of nodes has been reached");
                }
                break;
            case I_LOG_MESSAGE:
                logger.trace(payload);
                break;
            case I_TIME:
                logger.debug("Received time request from node {}; sending response", nodeId);
                sendMessage(new MySensorsMessage(nodeId, childSensorId, CommandType.INTERNAL, false, InternalType.I_TIME, Long.toString(System.currentTimeMillis() / 1000)));
                break;
            default:
                logger.error("Received unsupported internal message: {}", type);
        }
    }

    @Override
    public void onNodePresentation(int nodeId, int childSensorId, PresentationType type, String payload) {
        logger.debug("Received node presentation: {};{};{};{}", nodeId, childSensorId, type, payload);

        removeAssignableNodeId(nodeId);

        if (nodeId > 0) {
            if (childSensorId < 255) {
                final String id = Integer.toString(nodeId) + NODE_ID_SEPERATOR + Integer.toString(childSensorId);
                if (!hasDeviceProxy(id)) {
                    logger.debug("Detected new device: {} ({})", id, payload);
                    MySensorsNodeInfo info = nodeInfoMap.get(nodeId);
                    publishDeviceProxy(new MySensorsSensor(this, id, payload != null ? payload : SensorNames.getDefaultName(type), info != null ? info.getFirmwareVersion() : null)).addListener(new GenericFutureListener() {
                        @Override
                        public void operationComplete(Future future) throws Exception {
                        getDeviceProxy(id).setLastCheckin(System.currentTimeMillis());
                        }
                    });
                }
            } else {
                setNodeInfo(nodeId, type, payload);
            }
        }
    }

    @Override
    public void onNodeSet(int nodeId, int childSensorId, ValueType type, String payload) {
        logger.trace("Received node set: {};{};{};{}", nodeId, childSensorId, type, payload);

        removeAssignableNodeId(nodeId);

        String id = Integer.toString(nodeId) + NODE_ID_SEPERATOR + Integer.toString(childSensorId);

        if (hasDeviceProxy(id)) {
            MySensorsSensor sensor = (MySensorsSensor)getDeviceProxy(id);
            sensor.onVariableValueReceived(type, payload);
        } else {
            logger.error("Received variable set for unknown device: {}", nodeId);
        }
    }

    String getSensorSystem() {
        return sensorSystem;
    }

    String getNextNodeId() {
        String newNodeId = null;
        if (assignableNodeIds.size() > 0) {
            newNodeId = assignableNodeIds.get(0);
            assignableNodeIds.remove(0);
        }
        return newNodeId;
    }

    private void removeAssignableNodeId(int nodeId) {
        if (nodeId > 0 && nodeId < 255) {
            assignableNodeIds.remove(Integer.toString(nodeId));
        }
    }

    private void setNodeInfo(int nodeId, PresentationType type, String value) {
        MySensorsNodeInfo info = nodeInfoMap.get(nodeId);
        if (info == null) {
            info = new MySensorsNodeInfo();
            nodeInfoMap.put(nodeId, info);
        }
        switch (type) {
            case S_ARDUINO_NODE:
            case S_ARDUINO_REPEATER_NODE:
                logger.debug("Setting firmware version for node {}: {}", nodeId, value);
                info.setFirmwareVersion(value);
                break;
        }
    }
}
