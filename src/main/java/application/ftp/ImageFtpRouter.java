package application.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageFtpRouter extends RouteBuilder {

    /*相关参考demo
    * http://www.jianshu.com/p/3e505db9cd63
    * https://www.cnblogs.com/kanjiabin/p/5954833.html
    * http://blog.csdn.net/yinwenjie/article/details/51692340
    * */

    private Logger logger = LoggerFactory.getLogger(getClass());

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
        logger.info("ImageFtpRouter exec from: " + ftpHost + " to: " + ftpDir);
    }
}
