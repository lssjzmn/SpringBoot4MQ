package application.controllers;

import application.activemq.MessageReceiver;
import application.listeners.MessageReceiveListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Message;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageReceiveController implements MessageReceiveListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private LinkedBlockingQueue<Object> msgBlockingQueue = new LinkedBlockingQueue<>(100);

    @Autowired
    MessageReceiver messageReceiver;

    private boolean isRunning = true;

    @Override
    public void onMessageReceived(Message mqMapMessage) {
        try {
            msgBlockingQueue.put(mqMapMessage);
            int queueSize = msgBlockingQueue.size();
            if (queueSize > 5)
                logger.info("msgBlockingQueue size is :" + msgBlockingQueue.size());
        } catch (InterruptedException e) {
            logger.error("onMessageReceived,but msgBlockingQueue encountered a InterruptedException", e);
        }
    }

    @PostConstruct
    private void msgHandlerThread() {
        registerListener();
        final Thread messageHandlerThread = new MessageHandlerThread();
        messageHandlerThread.start();
    }

    @PreDestroy
    private void stopMsgHandler() {
        isRunning = false;
        messageReceiver.removeListener(this);
    }

    class MessageHandlerThread extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Object msgObject = msgBlockingQueue.take();
                    logger.info("MessageHandlerThread take a message...");
                } catch (InterruptedException e) {
                    logger.error("msgHandlerThread take message but encountered a InterruptedException", e);
                }

            }
        }
    }

    private void registerListener() {
        if (messageReceiver != null) {
            messageReceiver.registerListener(this);
        }
    }
}
