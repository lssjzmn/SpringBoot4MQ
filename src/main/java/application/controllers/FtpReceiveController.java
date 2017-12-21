package application.controllers;

import application.ftp.FtpProcessor;
import application.listeners.FtpReceiveListener;
import org.apache.camel.Message;
import org.apache.camel.component.file.GenericFileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.RandomAccessFile;

@Component
public class FtpReceiveController implements FtpReceiveListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    FtpProcessor ftpProcessor;

    @Override
    @SuppressWarnings("unchecked")
    public void onFtpReceived(Object object) {
        if (object instanceof Message) {
            GenericFileMessage<RandomAccessFile> fileMessage = (GenericFileMessage<RandomAccessFile>) object;
            String fileName = fileMessage.getGenericFile().getFileName();
            logger.info("get a file from server named:" + fileName);
        }
    }

    @PostConstruct
    private void registerListener() {
        ftpProcessor.registerListener(this);
    }
}
