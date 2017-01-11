/*
 *******************************************************************************
 * Copyright (c) 2017 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.mysensors.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySensorsEncoder extends MessageToByteEncoder<MySensorsMessage> {
    private static final Logger logger = LoggerFactory.getLogger(MySensorsEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext context, MySensorsMessage msg, ByteBuf buf) throws Exception {
        String s = msg.getNodeId() + ";" + msg.getChildSensorId() + ";" + msg.getCommand() + ";" + (msg.isAck() ? "1" : "0") + ";" + msg.getType() + ";";
        if (msg.hasPayload()) {
            s += msg.getPayload();
        }
        s += "\n";
        logger.debug("Sending message to gateway: {}", s);
        buf.writeBytes(s.getBytes(CharsetUtil.US_ASCII));
    }
}
