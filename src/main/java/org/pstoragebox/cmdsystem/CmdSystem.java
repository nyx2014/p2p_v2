package org.pstoragebox.cmdsystem;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.netsystem.NetworkService;
import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.FormatSystemPrint;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CmdSystem {
    public static void  runCmdSystem(FileSystem fileSystem) {
        controller = fileSystem;
        FormatSystemPrint.printMessage("Please enter the command");
        var notEnd = true;
        while (notEnd) {
            String input = FormatSystemPrint.getNextLine();
            String[] inputs = input.split(" ");
            switch (inputs[0]) {
                case "ls":
                    ls();
                    break;
                case "whoami":
                    FormatSystemPrint.printMessage(AutoIdGenerator.getId());
                    break;
                case "friends":
                    FormatSystemPrint.printMessage("There are "+NetSystem.getOnlineNodes()+" friend(s).");
                    for (var friendId : NetSystem.getOnlineId()) {
                        FormatSystemPrint.printMessage("friend: "+friendId);
                    }
                    break;
                case "conn":
                    if (inputs.length<3){
                        FormatSystemPrint.printMessage("Invalid input");break;
                    }
                    try {
                        TcpService.connTo(InetAddress.getByName(inputs[1]), inputs[2]);
                    } catch (UnknownHostException e) {
                        FormatSystemPrint.printMessage("UnknownHostException: "+inputs[1]);
                        e.printStackTrace();
                    }

                    FormatSystemPrint.printMessage("There are "+NetSystem.getOnlineNodes()+" friend(s).");
                    for (var friendId : NetSystem.getOnlineId()) {
                        FormatSystemPrint.printMessage("friend: "+friendId);
                    }
                    break;
                case "upload":
                    if (inputs.length<3){
                        FormatSystemPrint.printMessage("Invalid input");break;
                    }
                    upload(inputs[1], inputs[2]);
                    break;
                case "download":
                    if (inputs.length<3){
                        FormatSystemPrint.printMessage("Invalid input");break;
                    }
                    download(inputs[1], inputs[2]);
                    break;
                case "exit":
                    notEnd = false;
                    NetworkService.stopNetworkService();
                    break;
                case "help":
                    FormatSystemPrint.printMessage("ls          ：展示文件系统内文件列表");
                    FormatSystemPrint.printMessage("whoami      ：显示本结点ID");
                    FormatSystemPrint.printMessage("friends     ：列出已连接结点信息");
                    FormatSystemPrint.printMessage("conn        ：手动连接至结点");
                    FormatSystemPrint.printMessage("            ：conn ip nodeId");
                    FormatSystemPrint.printMessage("upload      ：上传文件");
                    FormatSystemPrint.printMessage("            ：upload filepath filename");
                    FormatSystemPrint.printMessage("download    ：下载文件");
                    FormatSystemPrint.printMessage("            ：download filename filepath");
                    FormatSystemPrint.printMessage("exit        ：退出系统");
                    break;
                default:
                    FormatSystemPrint.printMessage("Invalid input");
                    FormatSystemPrint.printMessage("Enter help to get help message");
                    break;
            }
        }
        System.exit(0);
    }

    private static void ls() {
        String[] fileNames = controller.lsCommand();
        for (String fileName:fileNames) {
            FormatSystemPrint.printMessage(fileName);
        }
    }

    private static void upload(String filePath,String fileName) {
        controller.uploadCommand(fileName,filePath);
    }

    private static void download(String fileName,String filePath) {
        controller.downloadCommand(fileName,filePath);

    }

    private static FileSystem controller;
}
