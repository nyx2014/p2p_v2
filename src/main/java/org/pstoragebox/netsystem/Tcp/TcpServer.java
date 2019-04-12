package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import org.pstoragebox.system.PStorageBox;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.FormatSystemPrint;

import java.io.IOException;

import static org.pstoragebox.tools.FormatSystemPrint.printError;
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
                    printRemoteMessage(remote, "Sent Block, Saving...");
                    try {
                        FileStream.writeFileBlockToRealSystem(waitForSNDFilePath, buffer.getBytes());
                    } catch (IOException e) {
                        printError("Failed saving block to disk.");
                    }
                    waitForSNDFilePath = null;
                }

                var buf_str = buffer.toString();
                switch (buf_str.substring(0, 3)) {
                    case "REQ":
                        final var param = buf_str.split("#");
                        printRemoteMessage(remote, "Request ID: " + param[1]);
                        printRemoteMessage(remote, "Requesting Block: " + param[2]);
                        try {
                            event.write(Buffer.buffer("RPL"));
                            Thread.sleep(200);
                            event.write(Buffer.buffer(FileStream.readFileBlockFromRealSystem(param[2])));
                        } catch (IOException e) {
                            printError("Failed read block from disk.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "SND":
                        waitForSNDFilePath = buf_str.substring(3);
                        printRemoteMessage(remote, "Sending Block: " + waitForSNDFilePath);
                        break;
                    case "INF":
                        printRemoteMessage(remote, "Want Latest Info.");
                        event.write(Buffer.buffer("INF" + PStorageBox.getFileSystem().getLogicalFileList()));
                        break;
                    case "UPD":
                        printRemoteMessage(remote, "Sent Latest Info.");
                        PStorageBox.getFileSystem().setLogicalFileList(buf_str.substring(3));
                        break;
                    default:
                        printRemoteMessage(remote, "Says: " + buffer.toString());
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