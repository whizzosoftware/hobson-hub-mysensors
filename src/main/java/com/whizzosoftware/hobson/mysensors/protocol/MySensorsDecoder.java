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
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MySensorsDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MySensorsDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() > 0) {
            byte[] b = new byte[buf.readableBytes()];
            buf.readBytes(b);
            String s = new String(b);
            logger.trace("Read: {}", s);

            String[] tokens = s.split(";");

            if (tokens.length >= 5) {
                try {
                    out.add(new MySensorsMessage(
                            Integer.parseInt(tokens[0]),
                            Integer.parseInt(tokens[1]),
                            Integer.parseInt(tokens[2]),
                            Integer.parseInt(tokens[3]) == 1,
                            Integer.parseInt(tokens[4]),
                            tokens.length >= 6 ? tokens[5] : null
                    ));
                } catch (NumberFormatException e) {
                    logger.error("Received invalid message: {}", s);
                }
            } else {
                logger.error("Received invalid message: {}", s);
            }
        }
    }
}
