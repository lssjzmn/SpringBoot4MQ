package application.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class ApacheFtpServer {

    private FtpServerFactory ftpServerFactory = new FtpServerFactory();
    private FtpServer ftpServer = ftpServerFactory.createServer();

    @PostConstruct
    public void initFTP() throws FtpException {
        ftpServer.start();
    }

    public FtpServerFactory getFtpServerFactory() {
        return ftpServerFactory;
    }

    public void setFtpServerFactory(FtpServerFactory ftpServerFactory) {
        this.ftpServerFactory = ftpServerFactory;
    }

    public FtpServer getFtpServer() {
        return ftpServer;
    }

    public void setFtpServer(FtpServer ftpServer) {
        this.ftpServer = ftpServer;
    }
}
