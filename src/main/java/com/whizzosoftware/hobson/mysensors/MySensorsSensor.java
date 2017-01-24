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

import com.whizzosoftware.hobson.api.device.DeviceType;
import com.whizzosoftware.hobson.api.device.HobsonDeviceDescriptor;
import com.whizzosoftware.hobson.api.device.proxy.AbstractHobsonDeviceProxy;
import com.whizzosoftware.hobson.api.plugin.HobsonPlugin;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.*;
import com.whizzosoftware.hobson.mysensors.protocol.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MySensorsSensor extends AbstractHobsonDeviceProxy {
    private static final Logger logger = LoggerFactory.getLogger(MySensorsSensor.class);

    private String firmwareVersion;

    MySensorsSensor(HobsonPlugin plugin, HobsonDeviceDescriptor dd) {
        this(plugin, dd.getContext().getDeviceId(), dd.getName(), dd.getManufacturerVersion());

        // publish any previously published variables
        if (dd.hasVariables()) {
            List<DeviceProxyVariable> vars = new ArrayList<>();
            for (DeviceVariableDescriptor dvd : dd.getVariables()) {
                vars.add(new DeviceProxyVariable(dvd.getContext(), dvd.getMask()));
            }
            publishVariables(vars);
        }
    }

    MySensorsSensor(HobsonPlugin plugin, String id, String defaultName, String firmwareVersion) {
        super(plugin, id, defaultName, DeviceType.SENSOR);
        this.firmwareVersion = firmwareVersion;
        logger.debug("Created sensor {} ({}) with firmware version {}", id, defaultName, firmwareVersion);
    }

    @Override
    public String getManufacturerName() {
        return "MySensors";
    }

    @Override
    public String getManufacturerVersion() {
        return firmwareVersion;
    }

    @Override
    public String getModelName() {
        return null;
    }

    @Override
    public String getPreferredVariableName() {
        return null;
    }

    @Override
    public void onDeviceConfigurationUpdate(Map<String,Object> config) {

    }

    @Override
    public void onSetVariables(Map<String, Object> values) {

    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void onStartup(String name, Map<String,Object> config) {

    }

    @Override
    protected TypedProperty[] getConfigurationPropertyTypes() {
        return null;
    }

    void onVariableValueReceived(ValueType type, String payload) {
        logger.debug("{} received variable value update: {}={}", getContext(), type, payload);
        boolean imperial = "I".equals(((MySensorsPlugin)getPlugin()).getSensorSystem());
        long now = System.currentTimeMillis();

        // if we receive a variable, the node has contacted us
        setLastCheckin(now);

        // set the appropriate variable
        switch (type) {
            case V_ARMED:
                publishAndSetVariable(VariableConstants.ARMED, VariableMask.READ_ONLY, (Integer.parseInt(payload) == 1), now);
                break;
            case V_DIRECTION:
                publishAndSetVariable(VariableConstants.WIND_DIRECTION_DEGREES, VariableMask.READ_ONLY, Integer.parseInt(payload), now);
                break;
            case V_GUST:
                publishAndSetVariable(imperial ? VariableConstants.WIND_GUST_MPH : VariableConstants.WIND_GUST_KMH, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_HUM:
                publishAndSetVariable(VariableConstants.RELATIVE_HUMIDITY, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_KWH:
                publishAndSetVariable(VariableConstants.ENERGY_CONSUMPTION_KWH, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_LOCK_STATUS:
                publishAndSetVariable(VariableConstants.ON, VariableMask.READ_ONLY, (Integer.parseInt(payload) == 1), now);
                break;
            case V_PERCENTAGE:
                publishAndSetVariable(VariableConstants.LEVEL, VariableMask.READ_ONLY, Integer.parseInt(payload), now);
                break;
            case V_POWER_FACTOR:
                publishAndSetVariable(VariableConstants.POWER_FACTOR, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_STATUS:
                publishAndSetVariable(VariableConstants.ON, VariableMask.READ_ONLY, (Integer.parseInt(payload) == 1), now);
                break;
            case V_TEMP:
                publishAndSetVariable(imperial ? VariableConstants.TEMP_F : VariableConstants.TEMP_C, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_TRIPPED:
                publishAndSetVariable(VariableConstants.ON, VariableMask.READ_ONLY, (Integer.parseInt(payload) == 1), now);
                break;
            case V_VA:
                publishAndSetVariable(VariableConstants.APPARENT_POWER_VA, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_VAR:
                publishAndSetVariable(VariableConstants.REACTIVE_POWER_VAR, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_WATT:
                publishAndSetVariable(VariableConstants.ENERGY_CONSUMPTION_WATTS, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            case V_WIND:
                publishAndSetVariable(imperial ? VariableConstants.WIND_SPEED_MPH : VariableConstants.WIND_SPEED_KMH, VariableMask.READ_ONLY, Double.parseDouble(payload), now);
                break;
            default:
                logger.error("Received value for unknown variable: {}", type);
        }
    }

    void onBatteryUpdate(int percent) {
        publishAndSetVariable(VariableConstants.BATTERY, VariableMask.READ_ONLY, percent, System.currentTimeMillis());
    }

    private void publishAndSetVariable(String name, VariableMask mask, Object value, long now) {
        if (!hasVariable(name)) {
            publishVariables(Collections.singletonList(new DeviceProxyVariable(DeviceVariableContext.create(getContext(), name), mask, value, now)));
        } else {
            setVariableValue(name, value, System.currentTimeMillis());
        }
    }
}
