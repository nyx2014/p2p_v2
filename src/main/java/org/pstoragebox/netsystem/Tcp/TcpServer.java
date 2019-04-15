package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import org.pstoragebox.system.PStorageBox;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.FileTransfer;
import org.pstoragebox.tools.FormatSystemPrint;

import java.io.IOException;

import static org.pstoragebox.tools.FormatSystemPrint.printError;
import static org.pstoragebox.tools.FormatSystemPrint.printRemoteMessage;

class TcpServer {
    private static NetServer netServer;
//    private static Buffer data = Buffer.buffer();
//
//    private static Boolean snd = false;
//    private static String filePath;

    TcpServer(Vertx vertx) {
        if (netServer == null) netServer = vertx.createNetServer();
        netServer.connectHandler(event -> {
            event.handler(buffer -> {
                final var remote = event.remoteAddress().host();

//                if (snd){
//                    printInfo("Continue receive...");
//                    if (data.toString(ISO_8859_1).length() < LogicalFile.blockSize){
//                        data.appendBuffer(buffer);
//                        if (data.toString(ISO_8859_1).length()>=LogicalFile.blockSize){
//                            printInfo("Received a block, save to disk");
//                            var block = data.toString(ISO_8859_1);
//                            printInfo("block:"+filePath+" size:"+block.length()+"bytes");
//                            try {
////                                printInfo("DEBUG:"+data.length());
////                                printInfo("DEBUG:"+data.toString().length());
////                                printInfo("DEBUG:"+data.toString().getBytes(ISO_8859_1).length);
//                                FileStream.writeFileBlockToRealSystem(filePath, block.getBytes(ISO_8859_1));
////                                FileStream.writeFileBlockToRealSystem(filePath, data.toString().getBytes(ISO_8859_1));
//                            } catch (IOException e) {
//                                printError("Failed saving block to disk.");
//                            }
//                            snd = false;
//                            filePath = null;
//                            data = null;
////                            data = Buffer.buffer();
//                        }
//                    }else {
//                        printError("err "+data.length());
//                    }
//                }

                final var buf_str = buffer.toString();
                switch (buf_str.substring(0, 3)) {
                    case "REQ":
                        final var blockId = buf_str.substring(3);
                        printRemoteMessage(remote, "Requesting Block: " + blockId);
                        try {
                            FileTransfer.send(remote, blockId, FileStream.readFileBlockFromRealSystem(blockId));
                        } catch (IOException e) {
                            printError(e.getMessage());
                        }
//                        try {
//                            event.write(Buffer.buffer("RPL"+
//                                    (new String(FileStream
//                                            .readFileBlockFromRealSystem( buf_str.substring(3) ),ISO_8859_1)) ));
//
//                        } catch (IOException e) {
//                            printError("Failed read block from disk.");
//                        }
                        break;
//                    case "SND":
//                        final var 问号 = buf_str.indexOf("?");
//                        filePath = buf_str.substring(3,问号);
//                        final var firstPart = Buffer.buffer(buf_str.substring(问号+1).getBytes(ISO_8859_1));
////                        final var firstPart = buffer.getString(问号+1,buffer.length(),"ISO_8859_1"); //new String(buf_str.substring(问号+1).getBytes(ISO_8859_1),ISO_8859_1);
//                        data = firstPart;
//                        snd = true;
////                        final var block = buf_str.substring(问号).getBytes(ISO_8859_1);
////                        waitForSNDFilePath = buf_str.substring(3);
//                        printRemoteMessage(remote, "Sending Block: " + filePath);
//                        printInfo("Start Receive "+ firstPart.length());
//                        break;
                    case "INF":
                        printRemoteMessage(remote, "Want Latest Info.");
                        event.write(Buffer.buffer("INF" + PStorageBox.getFileSystem().getLogicalFileList()));
                        break;
                    case "UPD":
                        printRemoteMessage(remote, "Sent Latest Info.");
                        PStorageBox.getFileSystem().setLogicalFileList(buf_str.substring(3));
                        break;
                    case "HLO":
                        printRemoteMessage(remote, "Says Hello: " + buf_str.substring(3));
                        event.write(Buffer.buffer("Hi! My addr is: " + event.localAddress()));//HLO
                        break;
                    default:
//                        printRemoteMessage(remote, "Says: " + buffer.toString().substring(0,3));
//                        event.write(Buffer.buffer("received! My addr is: " + event.localAddress()));
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