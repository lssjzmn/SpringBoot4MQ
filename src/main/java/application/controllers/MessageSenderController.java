package application.controllers;

import application.activemq.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class MessageSenderController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Timer sendMsgTimer = new Timer();

    @Autowired
    MessageSender messageSender;

    @PostConstruct
    private void startSendMessages() {
        sendMsgTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                messageSender.sendMessage();
                logger.info("messageSender send message");
            }
        }, 5000, 1200);
    }
}
