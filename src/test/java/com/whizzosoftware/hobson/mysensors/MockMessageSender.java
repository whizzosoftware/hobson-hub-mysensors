package com.whizzosoftware.hobson.mysensors;

import com.whizzosoftware.hobson.mysensors.protocol.MySensorsMessage;

import java.util.ArrayList;
import java.util.List;

public class MockMessageSender implements MessageSender {
    private List<MySensorsMessage> messages = new ArrayList<>();

    @Override
    public void doSendMessage(MySensorsMessage msg) {
        messages.add(msg);
    }

    public int getMessageCount() {
        return messages.size();
    }

    public MySensorsMessage getMessage(int ix) {
        return messages.get(ix);
    }

    public void clearMessages() {
        messages.clear();
    }
}
