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

import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class MySensorsDecoderTest {
    @Test
    public void testMessageWithPayload() throws Exception {
        List<Object> out = new ArrayList<>();
        MySensorsDecoder decoder = new MySensorsDecoder();
        decoder.decode(null, Unpooled.copiedBuffer("255;255;3;0;3;payload".getBytes()), out);
        assertEquals(1, out.size());
        MySensorsMessage m = (MySensorsMessage)out.get(0);
        assertEquals(255, m.getNodeId());
        assertEquals(255, m.getChildSensorId());
        assertEquals(3, m.getCommand());
        assertFalse(m.isAck());
        assertEquals(3, m.getType());
        assertEquals("payload", m.getPayload());
    }

    @Test
    public void testMessageWithoutPayload() throws Exception {
        List<Object> out = new ArrayList<>();
        MySensorsDecoder decoder = new MySensorsDecoder();
        decoder.decode(null, Unpooled.copiedBuffer("255;255;3;0;3;".getBytes()), out);
        assertEquals(1, out.size());
        MySensorsMessage m = (MySensorsMessage)out.get(0);
        assertEquals(255, m.getNodeId());
        assertEquals(255, m.getChildSensorId());
        assertEquals(3, m.getCommand());
        assertFalse(m.isAck());
        assertEquals(3, m.getType());
        assertNull(m.getPayload());
    }

    @Test
    public void testInvalidMessage() throws Exception {
        List<Object> out = new ArrayList<>();
        MySensorsDecoder decoder = new MySensorsDecoder();
        decoder.decode(null, Unpooled.copiedBuffer("0;255;3;0;9".getBytes()), out);
    }
}
