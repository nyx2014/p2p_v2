package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.tools.ByteMerge;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.FormatSystemPrint;

import static org.pstoragebox.tools.FormatSystemPrint.printRemoteMessage;

class TcpServer {
    private static NetServer netServer;
    private String waitForSNDFilePath = null;

    TcpServer(Vertx vertx) {
        if (netServer == null) netServer = vertx.createNetServer();
        netServer.connectHandler(event -> {
            event.handler(buffer -> {
                var remote = event.remoteAddress().host();
                if (waitForSNDFilePath != null) {
                    printRemoteMessage(remote,"Sent Block, Saving...");
                    FileStream.writeFileBlockToRealSystem(waitForSNDFilePath, buffer.getBytes());
                    waitForSNDFilePath = null;
                }

                var buf_str = buffer.toString();
                switch (buf_str.substring(0, 3)) {
                    case "REQ":
                        var param = buf_str.split("#");
                        System.out.println("REQ: remote id:" + param[1]);
                        System.out.println("REQ: req file:" + param[2]);
                        printRemoteMessage(remote,"Requesting Block: " + buf_str);
                        event.write(Buffer.buffer(
                                ByteMerge.byteMerger("REQ".getBytes(), FileStream.readFileBlockFromRealSystem(param[2]))));
                        break;
                    case "SND":
                        waitForSNDFilePath = buf_str.substring(3);
                        System.out.println("SND: filePath:" + waitForSNDFilePath);
                        printRemoteMessage(remote,"Sending Block: " + waitForSNDFilePath);
                        break;
                    case "INF":
                        event.write(Buffer.buffer(
                                ByteMerge.byteMerger("INF".getBytes(), new FileSystem("", "").getMyData())
                        ));
                        printRemoteMessage(remote,"Want Latest Info.");
                        break;
                    case "UPD":
                        new FileSystem("", "").updateData(buf_str.substring(3).getBytes());
                        printRemoteMessage(remote,"Sent Latest Info.");
                        break;
                    default:
                        printRemoteMessage(remote,"Says: " + buffer.toString());
                        event.write(Buffer.buffer("received! My addr is: " + event.localAddress()));
                        break;
                }
//                if (buf_str.startsWith("REQ")) {
//                    var param = buf_str.split("#");
//                    System.out.println("REQ: remote id:" + param[1]);
//                    System.out.println("REQ: req file:" + param[2]);
//                    FormatSystemPrint.printInfo("LOG:[" + event.remoteAddress().host() + "]Requesting Block: " + buf_str);
//                    event.write(Buffer.buffer(ByteMerge.byteMerger("REQ".getBytes(), FileStream.readFileBlockFromRealSystem(param[2]))));
//                } else if (buf_str.startsWith("SND")) {
//                    waitForSNDFilePath = buf_str.substring(3);
//                    System.out.println("SND: filePath:" + waitForSNDFilePath);
//                    FormatSystemPrint.printInfo("LOG:[" + event.remoteAddress().host() + "]Sending Block: " + waitForSNDFilePath);
//                } else if (buf_str.startsWith("INF")) {
//                    event.write(Buffer.buffer(
//                            ByteMerge.byteMerger("INF".getBytes(), new FileSystem("","").getMyData() )
//                    ));
//                    FormatSystemPrint.printInfo("INF:[" + event.remoteAddress().host() + "]Want Latest Info.");
//                } else if (buf_str.startsWith("UPD")) {
//                    event.write(Buffer.buffer(
//                            ByteMerge.byteMerger("INF".getBytes(), new FileSystem("","").getMyData() )
//                    ));
//                    FormatSystemPrint.printInfo("INF:[" + event.remoteAddress().host() + "]Want Latest Info.");
//                } else {
//                    System.out.println("LOG: Received message from [" + event.remoteAddress() + "]:" + buffer.toString());
//                    event.write(Buffer.buffer("received! my ip is: " + event.localAddress()));
//                }

            }).closeHandler(close -> FormatSystemPrint.printInfo("close socket."));
        });
        netServer.listen(TcpService.PORT, res -> {
            if (res.succeeded()) FormatSystemPrint.printInfo("start socket succeed.");
        });
    }

    void stopServer() {
        netServer.close(event -> {
            if (event.succeeded()) FormatSystemPrint.printInfo("server stopped.");
        });
    }
}