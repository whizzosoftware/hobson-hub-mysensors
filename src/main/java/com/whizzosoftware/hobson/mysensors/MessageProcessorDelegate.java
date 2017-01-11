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

import com.whizzosoftware.hobson.mysensors.protocol.InternalType;
import com.whizzosoftware.hobson.mysensors.protocol.MySensorsMessage;
import com.whizzosoftware.hobson.mysensors.protocol.PresentationType;
import com.whizzosoftware.hobson.mysensors.protocol.ValueType;

public interface MessageProcessorDelegate {
    void onInternalMessage(int nodeId, int childSensorId, InternalType type, String payload);
    void onNodePresentation(int nodeId, int childSensorId, PresentationType type, String payload);
    void onNodeSet(int nodeId, int childSensorId, ValueType type, String payload);
    void sendMessage(MySensorsMessage msg);
}
