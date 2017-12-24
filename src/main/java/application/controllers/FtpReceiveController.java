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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FtpReceiveController implements FtpReceiveListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> fileWriteArrayList = new CopyOnWriteArrayList<>();

    private ExecutorService copyOnWriteService = Executors.newFixedThreadPool(8);

    @Autowired
    FtpProcessor ftpProcessor;

    @Override
    @SuppressWarnings("unchecked")
    public void onFtpReceived(Object object) {
        if (object instanceof Message) {
            GenericFileMessage<RandomAccessFile> fileMessage = (GenericFileMessage<RandomAccessFile>) object;
            String fileName = fileMessage.getGenericFile().getFileName();
            fileWriteArrayList.add(fileName);
            copyOnWriteService.execute(new CopyRunner(fileWriteArrayList));
            copyOnWriteService.execute(new CopyRunner(fileWriteArrayList));
            copyOnWriteService.execute(new ReadRunner(fileWriteArrayList));
            copyOnWriteService.execute(new CopyRunner(fileWriteArrayList));
            copyOnWriteService.execute(new ReadRunner(fileWriteArrayList));
            copyOnWriteService.execute(new ReadRunner(fileWriteArrayList));
            copyOnWriteService.execute(new CopyRunner(fileWriteArrayList));
            copyOnWriteService.execute(new ReadRunner(fileWriteArrayList));
            logger.info("get a file from server named:" + fileWriteArrayList.get(0));
        }
    }

    class CopyRunner implements Runnable {

        private List<String> copyList = new ArrayList<>();

        public CopyRunner(List<String> copyList) {
            this.copyList = copyList;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++)
                this.copyList.add("addedString from " + Thread.currentThread().getName());
        }
    }

    class ReadRunner implements Runnable {

        private List<String> copyList = new ArrayList<>();

        public ReadRunner(List<String> copyList) {
            this.copyList = copyList;
        }

        @Override
        public void run() {
            for (String str : this.copyList) {
                logger.info("read from " + str + Thread.currentThread().getName());
            }
        }
    }

    @PostConstruct
    private void registerListener() {
        ftpProcessor.registerListener(this);
    }
}
