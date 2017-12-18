package application.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/server")
@RestController
public class ServerMappingController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/getobject", method = RequestMethod.GET)
    public Object getObjectFromServer() {
        return "Object";
    }

    @RequestMapping(value = "/postobject", method = RequestMethod.POST)
    public String postObjectFromServer(@RequestBody String object) {
        return "success";
    }
}
