package org.pstoragebox.netsystem.UdpGroupMulticast;

import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.FormatSystemPrint;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.pstoragebox.netsystem.UdpGroupMulticast.GroupMulticastService.GROUP_MU_ADDR;
import static org.pstoragebox.netsystem.UdpGroupMulticast.GroupMulticastService.GROUP_MU_PORT;
import static org.pstoragebox.tools.FormatSystemPrint.printError;

public class GpMuSend extends Thread {
    private volatile boolean flag = true;

    private static byte[] data;

    static {
        try {
            data = (InetAddress.getLocalHost().getHostAddress() + '#' + AutoIdGenerator.getId()).getBytes();
        } catch (UnknownHostException e) {
            printError("UnknownHostException");
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
            printError("NULL Id Exception");
        }
    }

    @Override
    public void run() {
        var socket = GroupMulticastService.getSocket();
        while (flag) {
            try {
//                System.out.println("LOG: currently founded " + TcpService.getOnlineClientsCount() + " near node(s).");
//                System.out.println("LOG: multicast sending");
                socket.send(new DatagramPacket(data, data.length,
                        InetAddress.getByName(GROUP_MU_ADDR), GROUP_MU_PORT));
                Thread.sleep(2000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    void stopThread() {
        flag = false;
    }

}
