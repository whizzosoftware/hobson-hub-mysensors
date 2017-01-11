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

public enum InternalType {
    I_BATTERY_LEVEL,
    I_TIME,
    I_VERSION,
    I_ID_REQUEST,
    I_ID_RESPONSE,
    I_INCLUSION_MODE,
    I_CONFIG,
    I_FIND_PARENT,
    I_FIND_PARENT_RESPONSE,
    I_LOG_MESSAGE,
    I_CHILDREN,
    I_SKETCH_NAME,
    I_SKETCH_VERSION,
    I_REBOOT,
    I_GATEWAY_READY,
    I_SIGNING_PRESENTATION,
    I_NONCE_REQUEST,
    I_NONCE_REPSONSE,
    I_HEARTBEAT_REQUEST,
    I_PRESENTATION,
    I_DISCOVER_REQUEST,
    I_DISCOVER_RESPONSE,
    I_HEARTBEAT_RESPONSE,
    I_LOCKED,
    I_PING,
    I_PONG,
    I_REGISTRATION_REQUEST,
    I_REGISTRATION_RESPONSE,
    I_DEBUG;

    private static InternalType[] allValues = values();
    public static InternalType fromOrdinal(int n) {return allValues[n];}
}
