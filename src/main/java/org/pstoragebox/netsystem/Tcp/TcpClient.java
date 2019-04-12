package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.system.PStorageBox;
import org.pstoragebox.tools.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.*;

class TcpClient {
    private NetClient netClient;
    //    private NetSocket netSocket;
    private String writeHandler;
    private String myId;
//    private Future<byte[]> resp;
    private Boolean reply = false;
    private Buffer replyBuffer = null;
//    private Future<String> inf;

    TcpClient(final String myId, Vertx vertx,final InetAddress targetIP) {
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
                    printInfo("Client netSocket handler >");

                    if (reply) {
                        printInfo("Receive remote reply data, Saving");
                        replyBuffer = event;
                        reply = false;
                    }
                    var buf_str = event.toString();
                    switch (buf_str.substring(0, 3)) {
                        case "REQ":
                            reply = true;
                            printInfo("remote replying");
//                            resp.complete(buf_str.substring(3).getBytes());
                            break;
                        case "INF":
                            PStorageBox.getFileSystem().setLogicalFileList(buf_str.substring(3));
                            break;
                        default:
                            printInfo("SERVER says: "+buf_str);
                            break;
                    }
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

    private void snd(final Vertx vertx,final String out){
        vertx.eventBus().send(writeHandler, Buffer.buffer(out));
    }

    void sendBlock(final Vertx vertx,final byte[] blockData,final String filePath) {
        snd(vertx,"SND"+filePath);
        try {
            Thread.sleep(200);
            vertx.eventBus().send(writeHandler,Buffer.buffer(blockData));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        vertx.eventBus().send(writeHandler, Buffer.buffer("SND" + filePath), ev -> vertx.eventBus().send(writeHandler,Buffer.buffer(blockData)));
    }

    byte[] sendRequest(final Vertx vertx, final String filePath) {
//        resp = Future.future();

        snd(vertx,"REQ#" + myId + "#" + filePath);
        byte[] result = null;
        while (replyBuffer!=null){
            result = replyBuffer.getBytes();
            replyBuffer = null;
        }
        return result;
//        return resp.result();
    }

    void sendLatestInfo(final Vertx vertx,final String info) {
        snd(vertx,"UPD"+info);
    }

//    void sendLatestInfo(byte[] info) {
//        netSocket.write(Buffer.buffer(ByteMerge.byteMerger("UPD".getBytes(),info)), ev -> netSocket.write(Buffer.buffer(info)));
//    }

    void sendInfoRequest(Vertx vertx) {
        snd(vertx,"INF");
    }

//    String sendInfoRequest(Vertx vertx) {
//        inf = Future.future(event -> printWarn("future handler:"+event.result()));
////        vertx.eventBus().send(writeHandler, Buffer.buffer("INF"),event -> {
//////            inf.complete( event.result().body().toString().getBytes());
////            printInfo("eventBus Send Callback handler");
////            if (!event.succeeded())
////                MyLogger.get().log(Level.SEVERE,event.cause().getMessage());
////            var s = event.result().body().toString();
////            printInfo(s);
////            if (!inf.tryComplete(s))
////                MyLogger.get().log(Level.SEVERE,inf.cause().getMessage());
////        });
//        vertx.eventBus().send(writeHandler, Buffer.buffer("INF"));
//        String rs = null;
//        while (inf.isComplete()){
//            rs = inf.result();
//        }
//        printWarn("SIO:"+rs);
//        return rs;
//    }
}
