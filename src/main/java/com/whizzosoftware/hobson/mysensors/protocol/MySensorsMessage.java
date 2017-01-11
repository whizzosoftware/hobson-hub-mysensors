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

public class MySensorsMessage {
    private int nodeId;
    private int childSensorId;
    private int command;
    private boolean ack;
    private int type;
    private String payload;

    public MySensorsMessage(int nodeId, int childSensorId, int command, boolean ack, int type, String payload) {
        this.nodeId = nodeId;
        this.childSensorId = childSensorId;
        this.command = command;
        this.ack = ack;
        this.type = type;
        this.payload = payload;
    }

    public MySensorsMessage(int nodeId, int childSensorId, CommandType command, boolean ack, InternalType type, String payload) {
        this(nodeId, childSensorId, command.ordinal(), ack, type.ordinal(), payload);
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getChildSensorId() {
        return childSensorId;
    }

    public int getCommand() {
        return command;
    }

    public boolean isAck() {
        return ack;
    }

    public MySensorsMessage setAck(boolean ack) {
        this.ack = ack;
        return this;
    }

    public int getType() {
        return type;
    }

    public boolean hasPayload() {
        return (payload != null);
    }

    public String getPayload() {
        return payload;
    }

    public String toString() {
        return nodeId + ";" + childSensorId + ";" + command + ";" + (ack ? "1" : "0") + ";" + type + ";" + (payload != null ? payload : "");
    }
}
