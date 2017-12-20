package application.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ImageFtpRouter extends RouteBuilder {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 这是camel上下文对象，整个路由的驱动全靠它了。
    private ModelCamelContext camelContext = new SpringCamelContext();

    @Autowired
    FtpProcessor ftpProcessor;

    @Value("${ftp.server.host}")
    private String ftpHost;

    @Value("${ftp.server.port}")
    private String ftpPort;

    @Value("${ftp.local.save.dir}")
    private String ftpDir;

    @Override
    public void configure() throws Exception {
        from(ftpHost).process(ftpProcessor).to(ftpDir);
    }

    @PostConstruct
    private void initRouter() throws Exception {
        // 启动route
        camelContext.start();
        // 一个完整的消息路由过程，加入到上下文中
        camelContext.addRoutes(this);
    }
}
