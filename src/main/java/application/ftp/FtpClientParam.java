package application.ftp;

import application.model.FileType;

/**
 * Created by guimu-work on 2018/1/8.
 */

public class FtpClientParam {

    private boolean isConnected = false;

    /*
    * 雷达："192.168.1.200"(低速车), "192.168.1.30"(高速车)
    * 相机："192.168.1.210"(低速车), "192.168.1.210"(高速车)
    * */
    private String host = "192.168.1.200";

    private Integer post = 21;

    private String userName = "gmRadar";//相机guimu

    private String password = "guimu";

    private String remoteFolder = "";

    private String localFolder = "c:\\detectData";

    private FileType fileType;

    public FtpClientParam(String host, Integer post, String userName, String password, String remoteFolder, String localFolder, FileType fileType) {
        this.host = host;
        this.post = post;
        this.userName = userName;
        this.password = password;
        this.remoteFolder = remoteFolder;
        this.localFolder = localFolder;
        this.fileType = fileType;
    }

    public FtpClientParam(String remoteFolder, String localFolder, String password, String userName, FileType fileType) {
        this.remoteFolder = remoteFolder;
        this.localFolder = localFolder;
        this.password = password;
        this.userName = userName;
        this.fileType = fileType;
    }

    public FtpClientParam(String host, String remoteFolder, String localFolder, FileType fileType) {
        this.host = host;
        this.remoteFolder = remoteFolder;
        this.localFolder = localFolder;
        this.fileType = fileType;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemoteFolder() {
        return remoteFolder;
    }

    public void setRemoteFolder(String remoteFolder) {
        this.remoteFolder = remoteFolder;
    }

    public String getLocalFolder() {
        return localFolder;
    }

    public void setLocalFolder(String localFolder) {
        this.localFolder = localFolder;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
