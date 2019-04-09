package org.pstoragebox.cmdsystem;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.netsystem.NetworkService;
import org.pstoragebox.tools.FormatSystemPrint;

public class CmdSystem {
    public static void  runCmdSystem(FileSystem fileSystem) {
        controller = fileSystem;
        FormatSystemPrint.printMessage("Please enter the command");

        FormatSystemPrint.printMessage("ls          ：展示文件系统内文件列表");
        FormatSystemPrint.printMessage("upload      ：上传文件");
        FormatSystemPrint.printMessage("            ：upload filepath filename");
        FormatSystemPrint.printMessage("download    ：下载文件");
        FormatSystemPrint.printMessage("            ：download filename filepath");
        FormatSystemPrint.printMessage("exit        ：退出系统");
        Boolean notEnd = true;
        while (notEnd) {
            String input = FormatSystemPrint.getNextLine();
            String[] inputs = input.split(" ");
            if (inputs[0].equals("ls")) {
                ls();
            }
            else if (inputs[0].equals("upload")){
                upload(inputs[1],inputs[2]);
            }
            else if (inputs[0].equals("download")){
                download(inputs[1],inputs[2]);
            }
            else if (inputs[0].equals("exit")){
                notEnd = false;
                NetworkService.stopNetworkService();
            }
            else if (inputs[0].equals("help")){
                FormatSystemPrint.printMessage("ls          ：展示文件系统内文件列表");
                FormatSystemPrint.printMessage("upload      ：上传文件");
                FormatSystemPrint.printMessage("            ：upload filepath filename");
                FormatSystemPrint.printMessage("download    ：下载文件");
                FormatSystemPrint.printMessage("            ：download filename filepath");
                FormatSystemPrint.printMessage("exit        ：退出系统");
            }
            else {
                FormatSystemPrint.printMessage("Invalid input");
                FormatSystemPrint.printMessage("Enter help to get help message");
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
