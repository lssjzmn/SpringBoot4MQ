package application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class CommandRestInvoker {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${server.net.url}")
    private String SERVER_URL;

    public void invokePostCommand(Object requestObject, String requestUrl) {
        if (requestObject == null)
            return;
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(SERVER_URL + requestUrl, requestObject, Object.class);
        if (responseEntity.getBody() != null) {
            processResponse(responseEntity.getBody());
        } else {
            logger.error(responseEntity.toString());
        }
    }

    public void invokeGetCommand(String requestUrl) {
        if (requestUrl == null)
            return;
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(SERVER_URL + requestUrl, Object.class);
        if (responseEntity.getBody() == null) {
            processResponse(responseEntity.getBody());
        } else {
            logger.error(responseEntity.toString());
        }
    }

    abstract void processResponse(Object object);

}
