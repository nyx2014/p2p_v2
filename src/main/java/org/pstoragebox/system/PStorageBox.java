package org.pstoragebox.system;

import org.fusesource.jansi.AnsiConsole;
import org.pstoragebox.cmdsystem.CmdSystem;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.netsystem.NetworkService;
import org.pstoragebox.tools.*;

import java.io.*;
import java.util.Objects;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.*;

public class PStorageBox {
    public static FileSystem getFileSystem(){
        return fileSystem;
    }

    public static void run() {
        AnsiConsole.systemInstall();
        initSystem();
        SystemGuarder.saveSystem();//?
        CmdSystem.runCmdSystem();
    }

    public static void exit() {
        printWarn("system will exit now.");
        SystemGuarder.saveSystem();//
        NetworkService.stopNetworkService();
        AnsiConsole.systemUninstall();
        System.exit(0);
    }

    private static void initSystem() {
        printMessage("系统初始化开始...");

        String systemID = null;
        try {
            systemID = AutoIdGenerator.getId();
        } catch (IOException e) {
            printError("本机识别码加载错误！");
            exit();
        }
        printMessage("UUID初始化完成: " + systemID);

        String localFilePath = null;
        try {
            localFilePath = LocalPathManager.getLocalFilePath();
        } catch (Exception e) {
            printError("共享文件夹目录初始化错误！");
            exit();
        }
        printMessage("共享文件夹目录初始化完成");

        fileSystem = new FileSystem(localFilePath, systemID);
        if (SystemGuarder.recoverSystem()){
            printMessage("系统恢复完成！");
        }else {
            printMessage("文件系统初始化完成");
        }

        NetworkService.startNetworkService();
        printMessage("网络系统初始化完成");
    }



    private static FileSystem fileSystem;
}
