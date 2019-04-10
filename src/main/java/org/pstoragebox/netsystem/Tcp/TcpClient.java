package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.ByteMerge;
import org.pstoragebox.tools.FormatSystemPrint;

import java.io.IOException;
import java.net.InetAddress;

import static org.pstoragebox.tools.FormatSystemPrint.*;

class TcpClient {
    private NetClient netClient;
    //    private NetSocket netSocket;
    private String writeHandler;
    private String myId;
    private Future<byte[]> resp;
    private Future<byte[]> inf;

    TcpClient(String myId, Vertx vertx, InetAddress targetIP) {
        this.myId = myId;
        netClient = vertx.createNetClient(new NetClientOptions().setReconnectAttempts(1));

        netClient.connect(TcpService.PORT, targetIP.getHostAddress(), conn -> {
            if (conn.succeeded()) {
                String id;
                try {
                    id = AutoIdGenerator.getId();
                } catch (IOException e) {
                    return;
                }
                var netSocket = conn.result();
                writeHandler = netSocket.writeHandlerID();
                netSocket.write(
                        Buffer.buffer("hello! I'm "
                                + id//netSocket.indicatedServerName()
                                + " and my ip is: "
                                + netSocket.localAddress()));
                netSocket.handler(event -> {
                    var buf_str = event.toString();
                    if (buf_str.startsWith("REQ")) resp.complete(buf_str.substring(3).getBytes());
                    else if (buf_str.startsWith("INF")) inf.complete(buf_str.substring(3).getBytes());
                    else System.out.println("SERVER says: " + buf_str);
                }).closeHandler(event -> {
                    printInfo("LOG: TcpClient Socket closed here.");
                    TcpService.disConnFrom(myId);
                });
            } else printError("failed connect Server.");
        });
    }

    void stopClient() {
//        netSocket.close();
        netClient.close();
    }

    void sendBlock(Vertx vertx, byte[] blockData, String filePath) {
        vertx.eventBus().send(writeHandler, Buffer.buffer("SND" + filePath), ev -> vertx.eventBus().send(writeHandler,Buffer.buffer(blockData)));
    }

    void sendLatestInfo(byte[] info) {
//        netSocket.write(Buffer.buffer(ByteMerge.byteMerger("UPD".getBytes(),info)), ev -> netSocket.write(Buffer.buffer(info)));
    }

    byte[] sendRequest(Vertx vertx, String filePath) {
        vertx.eventBus().send(writeHandler, Buffer.buffer("REQ#" + myId + "#" + filePath));
//        netSocket.write("REQ#" + myId + "#" + filePath);
        resp = Future.future();
        return resp.result();
    }

    byte[] sendInfoRequest(Vertx vertx) {

        inf = Future.future();
        vertx.eventBus().send(writeHandler, Buffer.buffer("INF"),event -> {
            inf.complete( event.result().body().toString().getBytes());
        });
        var rs = inf.result();
        printWarn(rs.toString());
        return rs;
    }
}
