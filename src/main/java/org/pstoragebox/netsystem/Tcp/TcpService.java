package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Vertx;
import org.pstoragebox.system.PStorageBox;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.pstoragebox.tools.FormatSystemPrint.printInfo;
import static org.pstoragebox.tools.FormatSystemPrint.printWarn;

public class TcpService {
    static final Integer PORT = 5555;
    private static TcpServer server;
    private static Map<String, TcpClient> clients = new HashMap<>();
    private static Vertx v;

    public TcpService(Vertx vertx) {
        v = vertx;
        server = new TcpServer(v);
    }

    public static void connTo(InetAddress targetIP, String targetId) {
        if (v == null) throw new NullPointerException();
        if (!clients.containsKey(targetId)) {
            printInfo("Found Node: " + targetId + " at " + targetIP);
            printInfo("trying conn to " + targetIP.getCanonicalHostName());
            clients.put(targetId, new TcpClient(targetId, v, targetIP));

//            ((TcpClient) clients.values().toArray()[0]).sendInfoRequest(v);

//            var it = clients.values().iterator();
//            if (it.hasNext()) PStorageBox.getFileSystem().updateData(it.next().sendInfoRequest());

            printInfo("currently connected " + clients.size() + " near node(s).");
//            System.out.println("LOG: currently connected " + clients.size() + " near node(s).");
        }
    }
    public static void sendinforeq(){
        ((TcpClient) clients.values().toArray()[0]).sendInfoRequest(v);
    }

    static void disConnFrom(String myId) {
        clients.remove(myId);
        printInfo("LOG: currently connected " + clients.size() + " near node(s).");
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
        printInfo("sendBlockTo:" + targetId + "#" + Arrays.toString(blockData) + "#" + filePath);
        if (!clients.containsKey(targetId)) return false;
        else {
            clients.get(targetId).sendBlock(v,blockData, filePath);

            var myData = PStorageBox.getFileSystem().getMyData();
            printWarn(myData.toString());
            clients.values().forEach(tcpClient -> tcpClient.sendLatestInfo(myData));
            return true;
        }
    }

    public static byte[] requestBlockTo(String targetId, String filePath) {
        return clients.containsKey(targetId) ? clients.get(targetId).sendRequest(v,filePath) : null;
    }
}
