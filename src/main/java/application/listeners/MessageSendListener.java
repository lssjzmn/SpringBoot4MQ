package application.listeners;

import javax.jms.Message;

public interface MessageSendListener {
    void onMessageSended(Message message);
}
