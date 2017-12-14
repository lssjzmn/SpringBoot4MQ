package application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CommandController extends CommandRestInvoker {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void sendRequestToServer() {
        invokeGetCommand("command/sendRadarRequest");
        //invokePostCommand("postEntity", "postobject");
    }

    @Override
    void processResponse(Object object) {
        if (object != null) {
            logger.info("received from server end: " + object);
        }
    }
}
