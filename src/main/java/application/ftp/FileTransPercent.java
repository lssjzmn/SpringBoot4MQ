package application.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileTransPercent {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Double percent = 0.0;

    private long totalSize = 0L;

    private long countSize = 0L;

    private String filename = "";

    private static final byte[] lock = new byte[0];

    private long startTime = 0l;
    private long endTime = 0l;

    private List<RemoteFileReceiveListener> fileReceiveListeners = new ArrayList<>();

    @PostConstruct
    public void initFlags() {
        reset();
    }

    @PreDestroy
    public void preDestroy() {
        reset();
        removeListners();
    }

    public void increaseSize(long increment, String filename) {
        synchronized (lock) {
            this.filename = filename;
            this.countSize = this.countSize + increment;
            this.percent = Math.floor(new Long(countSize * 100 / totalSize).doubleValue());
            notifyListners(this.filename, this.totalSize, this.countSize, this.percent);
            logger.info("file trans percent: " + this.percent + "%");
            if (this.percent >= 100.0) {
                endTime = System.currentTimeMillis();
                long totalTime = (endTime - startTime) / 1000;
                logger.info("file trans finished success! " + "total time: " + totalTime + "s."
                        + " avg speed :" + totalSize / 1024 / 1024 / totalTime + "mb/s");
                reset();
            }
        }
    }

    private void reset() {
        percent = 0.0;
        totalSize = 0L;
        countSize = 0L;
        this.filename = "";
        startTime = 0l;
        endTime = 0l;
    }

    private void notifyListners(String filename, long totalSize, long countSize, Double percent) {
        for (RemoteFileReceiveListener listener : fileReceiveListeners) {
            listener.onFileGet(filename, totalSize, countSize, percent);
        }
    }

    public void addListner(RemoteFileReceiveListener listener) {
        if (!fileReceiveListeners.contains(listener)) {
            fileReceiveListeners.add(listener);
        }
    }

    public void removeListner(RemoteFileReceiveListener listener) {
        if (fileReceiveListeners.contains(listener)) {
            fileReceiveListeners.remove(listener);
        }
    }

    private void removeListners() {
        if (fileReceiveListeners.size() > 0)
            fileReceiveListeners.clear();
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public synchronized void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
        startTime = System.currentTimeMillis();
    }
}
