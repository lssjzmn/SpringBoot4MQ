package application.ftp;

import application.listeners.FtpReceiveListener;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FtpProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<FtpReceiveListener> listeners = new ArrayList<>();

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        notifyFtpListeners(message);
    }

    private void notifyFtpListeners(Object object) {
        for (FtpReceiveListener ftpReceiveListener : listeners) {
            ftpReceiveListener.onFtpReceived(object);
        }
    }

    public void registerListener(FtpReceiveListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(FtpReceiveListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
