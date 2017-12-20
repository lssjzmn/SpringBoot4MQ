package application.controllers;

import application.ftp.FtpProcessor;
import application.listeners.FtpReceiveListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FtpReceiveController implements FtpReceiveListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    FtpProcessor ftpProcessor;

    @Override
    public void onFtpReceived(Object object) {

    }

    @PostConstruct
    private void registerListener() {
        ftpProcessor.registerListener(this);
    }
}
