package org.pstoragebox.netsystem.UdpGroupMulticast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GroupMulticastService{

    static final String GROUP_MU_ADDR = "230.0.0.1"; //"224.0.0.0";
    static final Integer GROUP_MU_PORT = 5001;
    private static GpMuSend gpMuSend = new GpMuSend();
    private static GpMuReceive gpMuReceive = new GpMuReceive();
    private static MulticastSocket socket;

    static {
        try {
            socket = new MulticastSocket(GROUP_MU_PORT); // 初始化MulticastSocket类并将端口号与之关联
            socket.setTimeToLive(1); // 设置组播数据报的发送范围为本地网络
            socket.setSoTimeout(10000); // 设置套接字的接收数据报的最长时间
            socket.joinGroup(InetAddress.getByName(GROUP_MU_ADDR)); // 加入此组播组
            socket.setLoopbackMode(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GroupMulticastService() {
        gpMuReceive.start();
        gpMuSend.start();
    }

    static MulticastSocket getSocket() {
        return socket;
    }

    public void stopService(){
        try {
            gpMuReceive.stopThread();
            Thread.sleep(100);
            gpMuSend.stopThread();
            Thread.sleep(100);
            socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        socket.leaveGroup(InetAddress.getByName(GROUP_MU_ADDR));
    }
}
