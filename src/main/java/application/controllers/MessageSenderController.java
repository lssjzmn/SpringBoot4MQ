package application.controllers;

import application.activemq.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
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
                messageSender.sendMessage(new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        MapMessage message = session.createMapMessage();
                        message.setString("name", "RobotState");
                        message.setLong("time", System.currentTimeMillis());
                        message.setDouble("postionX", 32.011);
                        message.setDouble("postionY", 112.001);
                        logger.info("messageSender send message", message);
                        System.out.println("messageSender send message" + message.toString());
                        return message;
                    }
                });
            }
        }, 5000, 5000);
    }
}
