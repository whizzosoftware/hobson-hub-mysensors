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

import com.whizzosoftware.hobson.api.device.MockDeviceManager;
import com.whizzosoftware.hobson.api.event.MockEventManager;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.mysensors.protocol.ValueType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MySensorsSensorTest {
    MockEventManager em;
    MockDeviceManager dm;
    MySensorsPlugin plugin;
    MySensorsSensor s;

    @Before
    public void setUp() throws Exception {
        em = new MockEventManager();
        dm = new MockDeviceManager();
        plugin = new MySensorsPlugin("plugin", "1.0", "plugin");
        plugin.setDeviceManager(dm);
        plugin.setEventManager(em);
        s = new MySensorsSensor(plugin, "1.1", "Sensor", "1.0");
        dm.publishDevice(s, null, null).sync();
    }

    @Test
    public void testOnVariableValueReceivedWaterLeakSensor() throws Exception {
        s.onVariableValueReceived(ValueType.V_TRIPPED, "0");
        DeviceVariableState v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.ON));
        assertNotNull(v);
        assertEquals(VariableConstants.ON, v.getContext().getName());
        assertFalse((boolean)v.getValue());

        s.onVariableValueReceived(ValueType.V_ARMED, "1");
        v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.ARMED));
        assertNotNull(v);
        assertEquals(VariableConstants.ARMED, v.getContext().getName());
        assertTrue((boolean)v.getValue());
    }

    @Test
    public void testOnVariableValueReceivedBinarySensor() throws Exception {
        s.onVariableValueReceived(ValueType.V_STATUS, "1");
        DeviceVariableState v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.ON));
        assertNotNull(v);
        assertEquals(VariableConstants.ON, v.getContext().getName());
        assertTrue((boolean)v.getValue());

        s.onVariableValueReceived(ValueType.V_WATT, "140.5");
        v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.ENERGY_CONSUMPTION_WATTS));
        assertNotNull(v);
        assertEquals(VariableConstants.ENERGY_CONSUMPTION_WATTS, v.getContext().getName());
        assertEquals(140.5, (double)v.getValue(), 0);
    }

    @Test
    public void testOnVariableValueReceivedDimmer() throws Exception {
        s.onVariableValueReceived(ValueType.V_PERCENTAGE, "50");
        DeviceVariableState v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.LEVEL));
        assertNotNull(v);
        assertEquals(VariableConstants.LEVEL, v.getContext().getName());
        assertEquals(50, (int)v.getValue());

        s.onVariableValueReceived(ValueType.V_WATT, "39.9");
        v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.ENERGY_CONSUMPTION_WATTS));
        assertNotNull(v);
        assertEquals(VariableConstants.ENERGY_CONSUMPTION_WATTS, v.getContext().getName());
        assertEquals(39.9, (double)v.getValue(), 0);
    }

    @Test
    public void testOnVariableValueReceivedHumidity() throws Exception {
        s.onVariableValueReceived(ValueType.V_HUM, "0.5");
        DeviceVariableState v = dm.getDeviceVariable(DeviceVariableContext.create(s.getContext(), VariableConstants.RELATIVE_HUMIDITY));
        assertNotNull(v);
        assertEquals(VariableConstants.RELATIVE_HUMIDITY, v.getContext().getName());
        assertEquals(0.5, (double)v.getValue(), 0);
    }
}
