package application.activemq;

import application.controllers.MessageReceiveController;
import application.listeners.MessageReceiveListener;
import application.pojobeans.DataEntity;
import application.reposervice.DataEntityService;
import application.redis.RedisDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

public class MessageReceiver extends MessageReceiveController implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<MessageReceiveListener> listeners = new ArrayList<>();

    @Autowired
    DataEntityService dataEntityService;

    @Autowired
    RedisDataService redisDataService;

    private int cnt = 1;

    @Override
    public void onMessage(Message message) {
        onMessageReceived(message);
    }

    private void notifyMessageListeners(Message message) {
        for (MessageReceiveListener messageReceiveListener : listeners) {
            messageReceiveListener.onMessageReceived(message);
        }
    }

    @Override
    public void processMessage(Object mqMapMessage) throws JMSException {
        if (mqMapMessage instanceof MapMessage) {
            MapMessage message = (MapMessage) mqMapMessage;
            logger.info("MessageHandlerThread take a message..." + message.toString());
            DataEntity dataEntity = new DataEntity();
            dataEntity.setContent(message.getString("name") + "lssjzmndata");
            dataEntityService.save(dataEntity);
            logger.info("#####第一次读取： " + dataEntityService.getOne(1000).getId());
            logger.info("#####第二次读取： " + dataEntityService.getOne(1001).getId());
            logger.info("#####第三次读取： " + dataEntityService.getOne(1002).getId());
            logger.info("#####第四次读取： " + dataEntityService.getOne(1003).getId());
            logger.info("#####第五次读取： " + dataEntityService.getOne(1004).getId());
            Long time0 = System.currentTimeMillis();
            logger.info("#####第六次读取： " + ((List) dataEntityService.getAllCache()).size());
            Long time1 = System.currentTimeMillis();
            System.out.println("###############" + (time1 - time0));
            //redisDataService.set("redisKeyName" + cnt++, "redisValue");
        }
    }

    public void registerListener(MessageReceiveListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(MessageReceiveListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
