package application.activemq;

import application.controllers.MessageReceiveController;
import application.listeners.MessageReceiveListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

public class MessageReceiver implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageReceiveController messageReceiveController;

    private List<MessageReceiveListener> listeners = new ArrayList<>();

    @Override
    public void onMessage(Message message) {
        processMessage(message);
    }

    private void processMessage(Message message) {
        for (MessageReceiveListener messageReceiveListener : listeners) {
            messageReceiveListener.onMessageReceived(message);
        }
    }

    public void registerListener(MessageReceiveListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(MessageReceiveListener listener){
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
