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

public enum ValueType {
    V_TEMP,
    V_HUM,
    V_STATUS,
    V_PERCENTAGE,
    V_PRESSURE,
    V_FORECAST,
    V_RAIN,
    V_RAINRATE,
    V_WIND,
    V_GUST,
    V_DIRECTION,
    V_UV,
    V_WEIGHT,
    V_DISTANCE,
    V_IMPEDANCE,
    V_ARMED,
    V_TRIPPED,
    V_WATT,
    V_KWH,
    V_SCENE_ON,
    V_SCENE_OFF,
    V_HVAC_FLOW_STATE,
    V_HVAC_SPEED,
    V_LIGHT_LEVEL,
    V_VAR1,
    V_VAR2,
    V_VAR3,
    V_VAR4,
    V_VAR5,
    V_UP,
    V_DOWN,
    V_STOP,
    V_IR_SEND,
    V_IR_RECEIVE,
    V_FLOW,
    V_VOLUME,
    V_LOCK_STATUS,
    V_LEVEL,
    V_VOLTAGE,
    V_CURRENT,
    V_RGB,
    V_RGBW,
    V_ID,
    V_UNIT_PREFIX,
    V_HVAC_SETPOINT_COOL,
    V_HVAC_SETPOINT_HEAT,
    V_HVAC_FLOW_MODE,
    V_TEXT,
    V_CUSTOM,
    V_POSITION,
    V_IR_RECORD,
    V_PH,
    V_ORP,
    V_EC,
    V_VAR,
    V_VA,
    V_POWER_FACTOR;

    private static ValueType[] allValues = values();
    public static ValueType fromOrdinal(int n) {return allValues[n];}
}
