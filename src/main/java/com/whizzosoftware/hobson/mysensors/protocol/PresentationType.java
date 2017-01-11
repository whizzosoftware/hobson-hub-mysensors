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

public enum PresentationType {
    S_DOOR,
    S_MOTION,
    S_SMOKE,
    S_BINARY,
    S_DIMMER,
    S_COVER,
    S_TEMP,
    S_HUM,
    S_BARO,
    S_WIND,
    S_RAIN,
    S_UV,
    S_WEIGHT,
    S_POWER,
    S_HEATER,
    S_DISTANCE,
    S_LIGHT_LEVEL,
    S_ARDUINO_NODE,
    S_ARDUINO_REPEATER_NODE,
    S_LOCK,
    S_IR,
    S_WATER,
    S_AIR_QUALITY,
    S_CUSTOM,
    S_DUST,
    S_SCENE_CONTROLLER,
    S_RGB_LIGHT,
    S_RGBW_LIGHT,
    S_COLOR_SENSOR,
    S_HVAC,
    S_MULTIMETER,
    S_SPRINKLER,
    S_WATER_LEAK,
    S_SOUND,
    S_VIBRATION,
    S_MOISTURE,
    S_INFO,
    S_GAS,
    S_GPS,
    S_WATER_QUALITY;

    private static PresentationType[] allValues = values();
    public static PresentationType fromOrdinal(int n) {return allValues[n];}
}
