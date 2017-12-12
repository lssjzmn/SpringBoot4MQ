package application.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class MessageSender {

    @Autowired
    @Qualifier("jmsTemplateBoot")
    JmsTemplate jmsTemplate;

    public void sendMessage() {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setLong("endTime", 999999999999L);
                mapMessage.setLong("recordId", 19999);
                mapMessage.setInt("trackSize", 99);
                mapMessage.setLong("imageSize", 9999L);
                mapMessage.setInt("action", 1);
                return mapMessage;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
