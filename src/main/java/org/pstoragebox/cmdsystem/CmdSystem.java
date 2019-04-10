package org.pstoragebox.cmdsystem;
import org.fusesource.jansi.AnsiConsole;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.netsystem.NetworkService;
import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.FormatSystemPrint;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.pstoragebox.tools.FormatSystemPrint.*;

public class CmdSystem {
    public static void  runCmdSystem(FileSystem fileSystem) {
        controller = fileSystem;
        help();
        printMessage("Please enter the command");
        var notEnd = true;
        while (notEnd) {
            String input = getNextLine();
            String[] inputs = input.split(" ");
            switch (inputs[0]) {
                case "cls":
                    cls();
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
                    printInfo("There are "+NetSystem.getOnlineNodes()+" friend(s).");
                    for (var friendId : NetSystem.getOnlineId()) {
                        printInfo("friend: "+friendId);
                    }
                    break;
                case "conn":
                    if (inputs.length<3){
                        printError("Invalid input");break;
                    }
                    try {
                        TcpService.connTo(InetAddress.getByName(inputs[1]), inputs[2]);
                    } catch (UnknownHostException e) {
                        printError("UnknownHostException: "+inputs[1]);
                        e.printStackTrace();
                    }

                    printInfo("There are "+NetSystem.getOnlineNodes()+" friend(s).");
                    for (var friendId : NetSystem.getOnlineId()) {
                        printInfo("friend: "+friendId);
                    }
                    break;
                case "upload":
                    if (inputs.length<3){
                        printError("Invalid input");break;
                    }
                    upload(inputs[1], inputs[2]);
                    break;
                case "download":
                    if (inputs.length<3){
                        printError("Invalid input");break;
                    }
                    download(inputs[1], inputs[2]);
                    break;
                case "exit":
                    notEnd = false;
                    NetworkService.stopNetworkService();
                    break;
                case "help":
                    help();
                    break;
                default:
                    printError("Invalid input");
                    printMessage("Enter help to get help message");
                    break;
            }
        }
        AnsiConsole.systemUninstall();
        System.exit(0);
    }

    private static void ls() {
        String[] fileNames = controller.lsCommand();
        for (String fileName:fileNames) {
            printMessage(fileName);
        }
    }

    private static void upload(String filePath,String fileName) {
        controller.uploadCommand(fileName,filePath);
    }

    private static void download(String fileName,String filePath) {
        controller.downloadCommand(fileName,filePath);
    }

    private static void help(){
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

    private static FileSystem controller;
}
