package com.whizzosoftware.hobson.mysensors.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MySensorsEncoderTest {
    @Test
    public void testEncode() throws Exception {
        ByteBuf buf = Unpooled.buffer();
        MySensorsEncoder e = new MySensorsEncoder();
        e.encode(null, new MySensorsMessage(255, 255, CommandType.INTERNAL, true, InternalType.I_DISCOVER_REQUEST, null), buf);
        assertEquals(16, buf.readableBytes());
        byte[] b = new byte[16];
        buf.readBytes(b);
        String s = new String(b);
        assertEquals("255;255;3;1;20;\n", s);
    }
}
