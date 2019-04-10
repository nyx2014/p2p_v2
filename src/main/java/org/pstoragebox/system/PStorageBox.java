package org.pstoragebox.system;

import org.fusesource.jansi.AnsiConsole;
import org.pstoragebox.cmdsystem.CmdSystem;
import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.netsystem.NetworkService;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.FormatSystemPrint;
import org.pstoragebox.tools.LocalPathManager;
import org.pstoragebox.tools.MyLogger;

import java.io.*;
import java.util.Objects;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.*;

public class PStorageBox {
    public static void run(){
        initSystem();
        saveSystem();
        CmdSystem.runCmdSystem(fileSystem);
    }

    private static void initSystem(){
        AnsiConsole.systemInstall();
        printMessage("系统初始化开始...");

        String systemID = null;
        try {
            systemID = AutoIdGenerator.getId();
            printMessage(systemID);
        } catch (IOException e) {
            printError("本机识别码加载错误！");
            printMessage("system will exit now.");
            AnsiConsole.systemUninstall();
            System.exit(-1);
        }
//        if (Objects.equals(systemID, "ID_GET_ERROR")){
//            MyLogger.getMyLogger().log(Level.FINER,"本机识别码加载错误！");
//        }
        printMessage("UUID初始化完成");

        String localFilePath = LocalPathManager.getLocalFilePath();
        if (localFilePath.equals("LOCAL_FILE_PATH_INTI_ERROR")){
            printError("共享文件夹目录初始化错误！");
        }
        printMessage("共享文件夹目录初始化完成");

        fileSystem = new FileSystem(localFilePath,systemID);
        printMessage("文件系统初始化完成");

        NetworkService.startNetworkService();
        printMessage("网络系统初始化完成");
    }

    private static void saveSystem(){
        try {
            File file = new File(configFilePath);
            if (!file.exists())
                if(!file.createNewFile())
                    printError("failed create new file");
            FileWriter fileWriter=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("SystemMessage123");
            bufferedWriter.close();
            fileWriter.close();
        }catch (Exception e){
            MyLogger.getMyLogger().log(Level.FINER,e.toString());
        }
    }

    private static void recoverSystem(){
        File file = new File(configFilePath);
        if (!file.exists()){
            MyLogger.getMyLogger().log(Level.FINER,"系统备份不存在");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER,e.toString());
        }
    }

    private static FileSystem fileSystem;
    private static final String configFilePath = "./etc/normalConfig.txt";
}
