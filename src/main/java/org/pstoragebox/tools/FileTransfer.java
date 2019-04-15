package org.pstoragebox.tools;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.pstoragebox.tools.FormatSystemPrint.printInfo;

public class FileTransfer {
    private static final Integer PORT = 33456;

    public static void receive() {
        try {
            final var server = new ServerSocket(PORT);
            new Thread(() -> {
                while (true) {
                    try {
                        printInfo("开始监听...");
                        var socket = server.accept();
                        printInfo("有传入链接!");
                        receiveFile(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void receiveFile(Socket socket) throws IOException {
        byte[] inputByte;
        var length = 0;
        try (DataInputStream din = new DataInputStream(socket.getInputStream());
             FileOutputStream fout = new FileOutputStream(
                     new File(LocalPathManager.getLocalFilePath() + "\\" + din.readUTF()))) {
            inputByte = new byte[1024];
            printInfo("开始接收数据...");
            while (true) {
                if (din != null) length = din.read(inputByte, 0, inputByte.length);
                if (length == -1) break;
//                System.out.println(length);
                fout.write(inputByte, 0, length);
                fout.flush();
            }
            printInfo("完成接收");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }
    }

    public static void send(final String aimIP, final String fileName, final byte[] data) {
        try (Socket socket = new Socket();
             DataOutputStream dout = new DataOutputStream(socket.getOutputStream())) {
            socket.connect(new InetSocketAddress(aimIP, PORT), 10 * 1000);
            dout.writeUTF(fileName);
            dout.write(data);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
