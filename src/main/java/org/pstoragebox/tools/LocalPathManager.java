package org.pstoragebox.tools;

import java.io.*;
import java.util.logging.Level;

public class LocalPathManager {

    public static String getLocalFilePath(){return localFilePath;}

    private static String initLocalFilePath(){
        String filepath="./etc/localFilePathConfig.txt";
        File file=new File(filepath);
        if (!file.exists()){
            try {
                FormatSystemPrint.printMessage("第一次启动系统需要配置共享文件夹路径");
                FormatSystemPrint.printMessage("请输入共享文件夹路径：");
                FormatSystemPrint.printHead();
                String localFilePath = FormatSystemPrint.getNextLine();
                File dic = new File(localFilePath);
                while (!dic.isDirectory()){
                    FormatSystemPrint.printMessage("文件夹不存在，请重新输入");
                    FormatSystemPrint.printMessage("请输入共享文件夹路径：");
                    FormatSystemPrint.printHead();
                    localFilePath = FormatSystemPrint.getNextLine();
                    dic = new File(localFilePath);
                }
                file.createNewFile();
                FileWriter fileWriter=new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(localFilePath);
                bufferedWriter.close();
                fileWriter.close();
                return localFilePath;
            } catch (Exception e) {
                MyLogger.getMyLogger().log(Level.FINER,e.toString());
            }
        }else{
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                return bufferedReader.readLine();
            } catch (Exception e) {
                MyLogger.getMyLogger().log(Level.FINER,e.toString());
            }
        }
        return "LOCAL_FILE_PATH_INTI_ERROR";
    }
    private static final String localFilePath = initLocalFilePath();
}
