package org.pstoragebox.netsystem.UdpGroupMulticast;

import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.tools.FormatSystemPrint;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class GpMuReceive extends Thread {
    private volatile boolean flag = true;

    @Override
    public void run() {
        var socket = GroupMulticastService.getSocket();
        var data = new byte[512];
        var packet = new DatagramPacket(data, data.length);
        while (flag) {
            try {
                socket.receive(packet);
                var msg = new String(packet.getData(), 0, packet.getLength()).split("#");
                if (!InetAddress.getLocalHost().getHostAddress().equals(msg[0])) TcpService.connTo(InetAddress.getByName(msg[0]),msg[1]);
            } catch (SocketException e) {
                FormatSystemPrint.printError("UdpSocketClosed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void stopThread() {
        flag = false;
    }
}
