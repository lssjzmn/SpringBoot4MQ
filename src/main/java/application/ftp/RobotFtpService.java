package application.ftp;

import application.model.FileType;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Created by guimu-work on 2018/1/9.
 */
public class RobotFtpService implements Callable<String> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private FTPFile file;

    private FtpClientParam ftpClientParam;

    private FTPClient ftpClient;

    private boolean isConnected = false;

    private boolean isRunning = true;

    private Integer filesNum = 0;

    private Integer filesCount = 0;

    private long totalSize = 0L;

    private long countSize = 0L;

    private Integer fileBlock = 1;

    private Integer fileStart = 0;

    private static final Integer BUF_SIZE = 1024 * 2;

    private FileTransPercent transPercent;

    public RobotFtpService(FtpClientParam ftpClientParam, Integer fileStart, Integer fileBlock, boolean isRunning, FileTransPercent transPercent) {
        this.ftpClientParam = ftpClientParam;
        this.isRunning = isRunning;
        this.fileBlock = fileBlock;
        this.fileStart = fileStart;
        this.transPercent = transPercent;
        ftpClient = new FTPClient();
    }

    @Override
    public String call() throws Exception {
        if (isRunning) {
            startTrans();
        }
        closeFtp();
        return "SUCCESS";
    }

    private boolean initFtpClient() {
        if (ftpClientParam == null)
            return false;
        if (connectFtpSvr()) {
            isConnected = true;
            return true;
        }
        return false;
    }

    public void startTrans() {
        if (!initFtpClient())
            return;
        if (isConnected) {
            downloadRobotFiles(ftpClient, ftpClientParam);
        } else {
            logger.error("ftp not connected during exec downloadRobotFiles!");
        }
    }

    private boolean connectFtpSvr() {
        Integer reply = 1;
        resetFlags();
        try {
            configFtpClient();
            ftpClient.connect(ftpClientParam.getHost(), ftpClientParam.getPost());
            reply = ftpClient.getReplyCode();//SERVICE_READY:220
            if (!FTPReply.isPositiveCompletion(reply)) {
                closeFtp();
                logger.info("ftp server reply problem, host is " + ftpClientParam.getHost()
                        + " replyCode : " + reply);
                return false;
            }
            if (ftpClient.login(ftpClientParam.getUserName(), ftpClientParam.getPassword())) {
                logger.info("ftp client login success, host is " + ftpClientParam.getHost()
                        + " replyCode : " + reply);
            } else {
                logger.info("ftp client login failed, host is " + ftpClientParam.getHost());
                closeFtp();
                return false;
            }

        } catch (Exception e) {
            logger.error("connectFtpSvr: " + e.getMessage());
            closeFtp();
            return false;
        }

        return true;
    }

    private void configFtpClient() throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setRemoteVerificationEnabled(false);
        ftpClient.setControlKeepAliveTimeout(120);
        ftpClient.setConnectTimeout(60000);
        ftpClient.setDataTimeout(60000);
        ftpClient.setBufferSize(4096);
        //ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

    private void downloadRobotFiles(FTPClient ftpClient, FtpClientParam ftpClientParam) {
        try {
            if (ftpClient.changeWorkingDirectory(ftpClientParam.getRemoteFolder())) {
                FTPFile[] ftpFiles = ftpClient.listFiles();
                File localFolder = new File(ftpClientParam.getLocalFolder() + "\\"
                        + ftpClientParam.getRemoteFolder());
                if (!localFolder.exists())
                    localFolder.mkdirs();
                executeDownload(ftpFiles, ftpClientParam, ftpClient);
            } else {
                logger.error("ftp client change working directory failed, " + ftpClientParam.getRemoteFolder());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void executeDownload(FTPFile[] ftpFiles, FtpClientParam ftpClientParam, FTPClient ftpClient) {
        filesNum = ftpFiles.length;
        if (filesNum == 0)
            return;
        totalSize = 0L;
        for (FTPFile file : ftpFiles) {
            totalSize += file.getSize();
        }
        transPercent.setTotalSize(totalSize);
        if (ftpClientParam.getFileType() == FileType.RADAR) {
            for (FTPFile file : ftpFiles) {
                retrieveFileStream(file, ftpClientParam, ftpClient);
            }
        } else if (ftpClientParam.getFileType() == FileType.IMAGE) {
            for (int i = fileStart; i < fileStart + fileBlock; i++) {
                if (i > filesNum)
                    break;
                retrieveFileStream(ftpFiles[i], ftpClientParam, ftpClient);
            }
        }
    }

    private void retrieveFile(FTPFile file, FtpClientParam ftpClientParam, FTPClient ftpClient) {
        long fileSize = file.getSize();
        File localFile = new File(ftpClientParam.getLocalFolder() + "\\"
                + ftpClientParam.getRemoteFolder() + "\\" + file.getName());
        if (localFile.exists())
            return;
        OutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localFile);
            ftpClient.retrieveFile(file.getName(), fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            filesCount = filesCount + 1;
            countSize = countSize + fileSize;
            transPercent.increaseSize(fileSize, file.getName());
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }

    private synchronized void retrieveFileStream(FTPFile file, FtpClientParam ftpClientParam, FTPClient ftpClient) {
        long fileSize = file.getSize();
        int length = 0;
        byte[] fileBuffer = new byte[BUF_SIZE];
        File localFile = new File(ftpClientParam.getLocalFolder() + "\\"
                + ftpClientParam.getRemoteFolder() + "\\" + file.getName());
        if (localFile.exists())
            return;
        OutputStream fileOutputStream = null;
        InputStream fileInputStream = null;
        try {
            fileOutputStream = new FileOutputStream(localFile);
            fileInputStream = ftpClient.retrieveFileStream(file.getName());
            while ((length = fileInputStream.read(fileBuffer)) != -1) {
                fileOutputStream.write(fileBuffer, 0, length);
            }
            fileOutputStream.close();
            fileInputStream.close();
            if (!ftpClient.completePendingCommand()) {
                logger.error("file download failed -> " + file.getName());
            } else {
                filesCount = filesCount + 1;
                countSize = countSize + fileSize;
                transPercent.increaseSize(fileSize, file.getName());
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }

    private void closeFtp() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                resetFlags();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void resetFlags() {
        filesNum = 0;
        filesCount = 0;
        totalSize = 0L;
        countSize = 0L;
        isConnected = false;
    }
}
