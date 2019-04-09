package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Vertx;
import org.pstoragebox.tools.FormatSystemPrint;
import org.pstoragebox.tools.MyLogger;

import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;

public class TcpService {
    static final Integer PORT = 5555;
    private static TcpServer server;
    private static Map<String, TcpClient> clients = new HashMap<>();
    private static Vertx v;

    public TcpService(Vertx vertx) {
        v = vertx;
        server = new TcpServer(v);
    }

    public static void connTo(InetAddress targetIP, String myId) {
        if (v == null) throw new NullPointerException();
        if (!clients.containsKey(myId)) {
            FormatSystemPrint.printInfo("LOG: trying conn to " + targetIP.getCanonicalHostName());
            clients.put(myId, new TcpClient(myId, v, targetIP));
            FormatSystemPrint.printInfo("LOG: currently connected " + clients.size() + " near node(s).");
//            System.out.println("LOG: currently connected " + clients.size() + " near node(s).");
        }
    }

    static void disConnFrom(String myId) {
        clients.remove(myId);
        FormatSystemPrint.printInfo("LOG: currently connected " + clients.size() + " near node(s).");
    }

    public void stopService() {
        clients.values().forEach(TcpClient::stopClient);
        try {
            Thread.sleep(200);
            server.stopServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Integer getOnlineClientsCount() {
        return clients.size();
    }

    public static Set<?> getOnlineClientList() {
        return clients.keySet();
    }

    public static Boolean sendBlockTo(String targetId, byte[] blockData, String filePath) {
        FormatSystemPrint.printInfo("sendBlockTo:"+targetId + "#" + Arrays.toString(blockData) + "#" + filePath);
        if (!clients.containsKey(targetId)) return false;
        else {
            clients.get(targetId).sendBlock(blockData, filePath);
            return true;
        }
    }

    public static byte[] requestBlockTo(String targetId, String filePath) {
        return clients.containsKey(targetId) ? clients.get(targetId).sendRequest(filePath) : null;
    }
}
