package application.ftp;

/**
 * Created by guimu-work on 2018/1/8.
 */
public interface RemoteFileReceiveListener {

    void onFileGet(String fileName, long totalSize, long countSize, double percent);

}
