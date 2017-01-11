package com.whizzosoftware.hobson.mysensors;

import com.whizzosoftware.hobson.api.action.MockActionManager;
import com.whizzosoftware.hobson.api.config.MockConfigurationManager;
import com.whizzosoftware.hobson.api.device.MockDeviceManager;
import com.whizzosoftware.hobson.api.event.MockEventManager;
import com.whizzosoftware.hobson.api.plugin.MockPluginManager;
import com.whizzosoftware.hobson.mysensors.protocol.MySensorsMessage;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class MySensorsPluginTest {
    @Test
    public void testNextNodeIdWithNoExistingDevices() {
        MockDeviceManager dm = new MockDeviceManager();
        MySensorsPlugin plugin = new MySensorsPlugin("plugin", "1.0", "plugin");
        plugin.setDeviceManager(dm);
        for (int i=1; i <= 254; i++) {
            assertEquals(Integer.toString(i), plugin.getNextNodeId());
        }
        assertNull(plugin.getNextNodeId());
    }

    @Test
    public void testNextNodeIdWithExistingDevices() throws Exception {
        MockDeviceManager dm = new MockDeviceManager();
        MockConfigurationManager cm = new MockConfigurationManager();
        MockPluginManager pm = new MockPluginManager();
        MockActionManager am = new MockActionManager();
        MockEventManager em = new MockEventManager();
        pm.setEventManager(em);
        pm.setConfigurationManager(cm);
        MySensorsPlugin plugin = new MySensorsPlugin("plugin", "1.0", "plugin");
        pm.addLocalPlugin(plugin);
        plugin.setDeviceManager(dm);
        plugin.setPluginManager(pm);
        plugin.setActionManager(am);
        plugin.onChannelData(new MySensorsMessage(1, 1, 0, true, 32, "Water Leak"));
        plugin.onChannelData(new MySensorsMessage(254, 1, 0, true, 32, "Water Leak"));
        plugin.onStartup(null);
        assertEquals("2", plugin.getNextNodeId());
    }

    @Test
    public void testACK() {
//        MockDeviceManager dm = new MockDeviceManager();
//        MockEventManager em = new MockEventManager();
//        MockMessageSender sender = new MockMessageSender();
//        MySensorsPlugin p = new MySensorsPlugin("plugin", "1.0", "plugin");
//        p.setDeviceManager(dm);
//        p.setEventManager(em);
//        p.setMessageSender(sender);
//        assertEquals(0, sender.getMessageCount());
//
//        // verify set with ACK is not acknowledged if device doesn't exist
//        assertEquals(0, sender.getMessageCount());
//        p.onChannelData(new MySensorsMessage(1, 1, 1, true, 16, "0"));
//        assertEquals(0, sender.getMessageCount());
//
//        // verify internal ACK works
//        p.onChannelData(new MySensorsMessage(1, 1, 3, true, 0, "100"));
//        assertEquals(1, sender.getMessageCount());
//        MySensorsMessage m = sender.getMessage(0);
//        assertEquals("1;1;3;1;0;100", m.toString());
//        sender.clearMessages();
//
//        // verify no internal ACK works
//        p.onChannelData(new MySensorsMessage(1, 1, 3, false, 0, "100"));
//        assertEquals(0, sender.getMessageCount());
//
//        // verify presentation ACK works
//        assertEquals(0, sender.getMessageCount());
//        p.onChannelData(new MySensorsMessage(1, 1, 0, true, 32, "Water Leak"));
//        assertEquals(1, sender.getMessageCount());
//        m = sender.getMessage(0);
//        assertEquals("1;1;0;1;32;Water Leak", m.toString());
//        sender.clearMessages();
//
//        // verify no presentation ACK works
//        assertEquals(0, sender.getMessageCount());
//        p.onChannelData(new MySensorsMessage(1, 1, 0, false, 32, "Water Leak"));
//        assertEquals(0, sender.getMessageCount());
//
//        // verify set ACK works
//        assertEquals(0, sender.getMessageCount());
//        p.onChannelData(new MySensorsMessage(1, 1, 1, true, 16, "0"));
//        assertEquals(1, sender.getMessageCount());
//        m = sender.getMessage(0);
//        assertEquals("1;1;1;1;16;0", m.toString());
//        sender.clearMessages();
//
//        // verify no set ACK works
//        assertEquals(0, sender.getMessageCount());
//        p.onChannelData(new MySensorsMessage(1, 1, 1, false, 16, "0"));
//        assertEquals(0, sender.getMessageCount());
    }
}
