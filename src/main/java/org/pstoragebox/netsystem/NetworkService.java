package org.pstoragebox.netsystem;

import io.vertx.core.Vertx;
import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.netsystem.UdpGroupMulticast.GroupMulticastService;

public class NetworkService {
    private static TcpService tcpService;
    private static GroupMulticastService groupMulticastService;
    public static void startNetworkService(){
        System.out.println("start");
        var vert = Vertx.vertx();
        tcpService = new TcpService(vert);
        groupMulticastService = new GroupMulticastService();
    }

    public static void stopNetworkService(){
        tcpService.stopService();
        groupMulticastService.stopService();
        System.out.println("stop.");
    }
}
