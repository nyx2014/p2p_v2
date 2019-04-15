package org.pstoragebox.netsystem;

import io.vertx.core.Vertx;
import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.netsystem.UdpGroupMulticast.GroupMulticastService;
import org.pstoragebox.tools.FileTransfer;

import static org.pstoragebox.tools.FormatSystemPrint.printInfo;

public class NetworkService {
    private static TcpService tcpService;
    private static GroupMulticastService groupMulticastService;
    public static void startNetworkService(){
        printInfo("Network Service starting...");
        var vert = Vertx.vertx();
        tcpService = new TcpService(vert);
        groupMulticastService = new GroupMulticastService();
        FileTransfer.receive();
        printInfo("Network Service started");
    }

    public static void stopNetworkService(){
        tcpService.stopService();
        groupMulticastService.stopService();
        printInfo("Network Service Stopped.");
    }
}
