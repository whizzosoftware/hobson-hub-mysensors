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

import com.whizzosoftware.hobson.mysensors.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MySensorsMessageProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    void process(MessageProcessorDelegate delegate, MySensorsMessage m) {
        logger.trace("Processing message: {}", m);

        CommandType cmd = CommandType.fromOrdinal(m.getCommand());
        switch (cmd) {
            case PRESENTATION:
                try {
                    delegate.onNodePresentation(m.getNodeId(), m.getChildSensorId(), PresentationType.fromOrdinal(m.getType()), m.getPayload());
                } catch (ArrayIndexOutOfBoundsException e) {
                    logger.error("Received invalid presentation ID: {}", m);
                }
                break;
            case SET:
                try {
                    delegate.onNodeSet(m.getNodeId(), m.getChildSensorId(), ValueType.fromOrdinal(m.getType()), m.getPayload());
                } catch (ArrayIndexOutOfBoundsException e) {
                    logger.error("Received invalid value ID: {}", m);
                }
                break;
            case INTERNAL:
                try {
                    delegate.onInternalMessage(m.getNodeId(), m.getChildSensorId(), InternalType.fromOrdinal(m.getType()), m.getPayload());
                } catch (ArrayIndexOutOfBoundsException e) {
                    logger.error("Received invalid internal ID: {}", m);
                }
                break;
            default:
                logger.debug("Invalid command: {}", m.getCommand());
        }
    }
}
