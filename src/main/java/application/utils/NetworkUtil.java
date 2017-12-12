package application.utils;

import java.net.InetAddress;

/**
 * Created by guimu-yh on 2017-03-03.
 */
public class NetworkUtil {

    private static String testBaiduInetAddr = "www.baidu.com";//需要连接的IP地址
    /**
     * 传入需要连接的IP，返回是否连接成功
     * @param remoteInetAddr
     * @return
     */
    public static boolean isReachable(String remoteInetAddr) {
        Long startTime=System.currentTimeMillis();
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(remoteInetAddr);
            reachable = address.isReachable(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long endTime=System.currentTimeMillis();
        Long duringTime=endTime-startTime;
        System.out.println(duringTime);
        return reachable;
    }

    public static boolean isInternetAvailable() {
        Long startTime=System.currentTimeMillis();
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(testBaiduInetAddr);
            reachable = address.isReachable(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long endTime=System.currentTimeMillis();
        Long duringTime=endTime-startTime;
        System.out.println(duringTime);
        return reachable;
    }


//    public static void view(String[] args) {
//        URL url = null;
//        Boolean bon = false;
//        try {
//            url = new URL("http://baiddddddu.com/");
//            InputStream in = url.openStream();//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream
//            System.out.println("连接正常");
//            in.close();//关闭此输入流并释放与该流关联的所有系统资源。
//        } catch (IOException e) {
//            System.out.println("无法连接到：" + url.toString());
//        }
//        bon = isReachable(remoteInetAddr);
//        System.out.println("pingIP：" + bon);
//    }
}
