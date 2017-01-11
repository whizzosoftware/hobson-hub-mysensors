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

import com.whizzosoftware.hobson.mysensors.protocol.PresentationType;

public class SensorNames {
    static public String getDefaultName(PresentationType type) {
        switch (type) {
            case S_DOOR:
                return "Door sensor";
            case S_MOTION:
                return "Motion sensor";
            case S_SMOKE:
                return "Smoke sensor";
            case S_BINARY:
                return "Generic binary device";
            case S_DIMMER:
                return "Dimmer";
            case S_COVER:
                return "Shade control";
            case S_TEMP:
                return "Temperature sensor";
            case S_HUM:
                return "Humidity sensor";
            case S_BARO:
                return "Barometric sensor";
            case S_WIND:
                return "Wind sensor";
            case S_RAIN:
                return "Rain sensor";
            case S_UV:
                return "UV sensor";
            case S_WEIGHT:
                return "Weight sensor";
            case S_POWER:
                return "Power monitor";
            case S_HEATER:
                return "Heater";
            case S_DISTANCE:
                return "Distance sensor";
            case S_LIGHT_LEVEL:
                return "Light sensor";
            case S_ARDUINO_NODE:
                return "Arduino node";
            case S_ARDUINO_REPEATER_NODE:
                return "Arduino repeater node";
            case S_LOCK:
                return "Lock";
            case S_IR:
                return "Ir device";
            case S_WATER:
                return "Water meter";
            case S_AIR_QUALITY:
                return "Air quality sensor";
            case S_CUSTOM:
                return "Custom sensor";
            case S_DUST:
                return "Dust level sensor";
            case S_SCENE_CONTROLLER:
                return "Scene controller";
            case S_RGB_LIGHT:
                return "RGB light";
            case S_RGBW_LIGHT:
                return "RGBW light";
            case S_COLOR_SENSOR:
                return "Color sensor";
            case S_HVAC:
                return "HVAC device";
            case S_MULTIMETER:
                return "Multimeter";
            case S_SPRINKLER:
                return "Sprinkler";
            case S_WATER_LEAK:
                return "Water leak sensor";
            case S_SOUND:
                return "Sound sensor";
            case S_VIBRATION:
                return "Vibration sensor";
            case S_MOISTURE:
                return "Moisture sensor";
            case S_INFO:
                return "LCD text device";
            case S_GAS:
                return "Gas meter";
            case S_GPS:
                return "GPS sensor";
            case S_WATER_QUALITY:
                return "Water quality sensor";
            default:
                return "Unknown sensor";
        }
    }
}
