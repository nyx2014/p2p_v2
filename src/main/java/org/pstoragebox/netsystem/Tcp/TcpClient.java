package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.net.InetAddress;

class TcpClient {
    private NetClient netClient;
    private NetSocket netSocket;
    private String myId;
    private Future<byte[]> resp;

    TcpClient(String myId, Vertx vertx, InetAddress targetIP) {
        this.myId = myId;
        netClient = vertx.createNetClient(new NetClientOptions().setReconnectAttempts(1));
        netClient.connect(TcpService.PORT, targetIP.getHostAddress(), conn -> {
            if (conn.succeeded()) {
                netSocket = conn.result();
                netSocket.write(
                        Buffer.buffer("hello! I'm"
                                + netSocket.indicatedServerName()
                                + " and my ip is: "
                                + netSocket.localAddress()));
                netSocket.handler(event -> {
                    var buf_str = event.toString();
                    if (buf_str.startsWith("REQ")) resp.complete(buf_str.substring(3).getBytes());
                    else System.out.println("SERVER says: " + buf_str);
                });
                netSocket.closeHandler(event -> {
                    System.out.println("LOG: TcpClient Socket closed here.");
                    TcpService.disConnFrom(myId);
                });
            } else System.out.println("failed connect Server.");
        });
    }

    void stopClient() {
        netSocket.close();
        netClient.close();
    }

    void sendBlock(byte[] blockData, String filePath) {
        netSocket.write(Buffer.buffer("SND" + filePath), ev -> netSocket.write(Buffer.buffer(blockData)));
    }

    byte[] sendRequest(String filePath) {
        netSocket.write("REQ#" + myId + "#" + filePath);
        resp = Future.future();
        return resp.result();
    }
}
