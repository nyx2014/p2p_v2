package org.pstoragebox.cmdsystem;
import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.system.PStorageBox;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.SystemGuarder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.pstoragebox.tools.FormatSystemPrint.*;

public class CmdSystem {
    public static void  runCmdSystem() {
        help();
        printMessage("Please enter command");
//        printHead();
        while (true) {
            var inputs = getNextLine().split(" ");
            if (inputs.length!=0) switch (inputs[0]) {
                case "cls":
                    cls();
                    break;
                case "push":
                    TcpService.pushInfo();
                    break;
//                case "sh":
//                    printInfo(PStorageBox.getFileSystem().getLogicalFileList());
//                    break;
                case "req":
                    TcpService.sendInfoReq();
                    break;
                case "ls":
                    ls();
                    break;
                case "whoami":
                    String id;
                    try {
                        id = AutoIdGenerator.getId();
                        printInfo(id);
                    } catch (IOException e) {
                        printError(e.getMessage());
                    }
                    break;
                case "friends":
                    printInfo("There are " + TcpService.getOnlineClientsCount() + " friend(s).");
                    for (var friendId : NetSystem.getOnlineId()) {
                        printInfo("friend: " + friendId);
                    }
                    break;
                case "conn":
                    if (inputs.length < 3) {
                        printError("Invalid input");
                        break;
                    }
                    try {
                        TcpService.connTo(InetAddress.getByName(inputs[1]), inputs[2]);
                    } catch (UnknownHostException e) {
                        printError("UnknownHostException: " + inputs[1]);
                        return;
                    }

                    printInfo("There are " + TcpService.getOnlineClientsCount() + " friend(s).");
                    for (var friendId : NetSystem.getOnlineId()) {
                        printInfo("friend: " + friendId);
                    }
                    break;
                case "upload":
                    if (inputs.length < 3) {
                        printError("Invalid input");
                        break;
                    }
                    upload(inputs[1], inputs[2]);
                    break;
                case "download":
                    if (inputs.length < 3) {
                        printError("Invalid input");
                        break;
                    }
                    download(inputs[1], inputs[2]);
                    break;
                case "exit":
                    PStorageBox.exit();
                case "help":
                    help();
                    break;
                default:
                    printError("Invalid input");
                    printMessage("Enter help to get help message");
                    break;
            }
        }
    }

    private static void ls() {
        String[] fileNames = PStorageBox.getFileSystem().lsCommand();
        for (String fileName:fileNames) {
            printMessage(fileName);
        }
    }

    private static void upload(String filePath,String fileName) {
        try {
            PStorageBox.getFileSystem().uploadCommand(fileName,filePath);
            SystemGuarder.saveSystem();
        } catch (IOException e) {
            printError("发生错误，上传失败");
        }
    }

    private static void download(String fileName,String filePath) {
        try {
            PStorageBox.getFileSystem().downloadCommand(fileName,filePath);
        } catch (IOException e) {
            printError("下载失败:"+e.getMessage());
        }
    }

    private static void help(){
        printMessage("push        ：发送自己的fileList");
        printMessage("req         ：请求fileList");
        printMessage("cls         ：清屏");
        printMessage("ls          ：展示文件系统内文件列表");
        printMessage("whoami      ：显示本结点ID");
        printMessage("friends     ：列出已连接结点信息");
        printMessage("conn        ：手动连接至结点");
        printMessage("            ：conn ip nodeId");
        printMessage("upload      ：上传文件");
        printMessage("            ：upload filepath filename");
        printMessage("download    ：下载文件");
        printMessage("            ：download filename filepath");
        printMessage("exit        ：退出系统");
    }

}
