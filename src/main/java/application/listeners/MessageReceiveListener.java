package application.listeners;

import javax.jms.Message;

public interface MessageReceiveListener {
    void onMessageReceived(Message mqMapMessage);
}
