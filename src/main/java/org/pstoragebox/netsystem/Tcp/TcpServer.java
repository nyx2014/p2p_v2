package org.pstoragebox.netsystem.Tcp;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import org.pstoragebox.tools.ByteMerge;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.FormatSystemPrint;

class TcpServer {
    private static NetServer netServer;
    private String waitForSNDFilePath = null;

    TcpServer(Vertx vertx) {
        if (netServer == null) netServer = vertx.createNetServer();
        netServer.connectHandler(event -> {
            event.handler(buffer -> {
                if (waitForSNDFilePath != null) {
                    FormatSystemPrint.printInfo("LOG: TODO: save buffer to disk");
//                    buffer;
                    waitForSNDFilePath = null;
                }

                var buf_str = buffer.toString();
                if (buf_str.startsWith("REQ")) {
                    var param = buf_str.split("#");
                    System.out.println("REQ: remote id:" + param[1]);
                    System.out.println("REQ: req file:" + param[2]);
                    FormatSystemPrint.printInfo("LOG:[" + event.remoteAddress().host() + "]Requesting Block: " + buf_str);
                    event.write(Buffer.buffer(ByteMerge.byteMerger("REQ".getBytes(), FileStream.readFileBlockFromRealSystem(param[2]))));
                } else if (buf_str.startsWith("SND")) {
                    waitForSNDFilePath = buf_str.substring(3);
                    System.out.println("SND: filePath:" + waitForSNDFilePath);
                    FormatSystemPrint.printInfo("LOG:[" + event.remoteAddress().host() + "]Sending Block: " + waitForSNDFilePath);
                } else {
                    System.out.println("LOG: Received message from [" + event.remoteAddress() + "]:" + buffer.toString());
                    event.write(Buffer.buffer("received! my ip is: " + event.localAddress()));
                }

            });
            event.closeHandler(close -> FormatSystemPrint.printInfo("close socket."));
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