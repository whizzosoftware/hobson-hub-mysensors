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

public enum CommandType {
    PRESENTATION,
    SET,
    REQ,
    INTERNAL,
    STREAM;

    private static CommandType[] allValues = values();
    public static CommandType fromOrdinal(int n) {return allValues[n];}
}
