package org.pstoragebox.tools;

import java.io.*;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.*;

public class LocalPathManager {

    public static String getLocalFilePath() throws IOException {
        if (localFilePath == null)
            throw new IOException("LOCAL_FILE_PATH_INIT_ERROR");
        return localFilePath;
    }

    private static String initLocalFilePath() {
//        String filepath="./etc/localFilePathConfig.txt";

        var file = new File(ConfigPathTool.get("localFilePathConfig"));
        if (!file.exists()) {
            try {
                printMessage("第一次启动系统需要配置共享文件夹路径");
                printMessage("请输入共享文件夹路径：");
//                printHead();
                String localFilePath = getNextLine();
                File dic = new File(localFilePath);
                while (!dic.isDirectory()) {
                    printMessage("文件夹不存在，请重新输入");
                    printMessage("请输入共享文件夹路径：");
//                    printHead();
                    localFilePath = getNextLine();
                    dic = new File(localFilePath);
                }
                if (!file.createNewFile()) return null;
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(localFilePath);
                bufferedWriter.close();
                fileWriter.close();
                return localFilePath;
            } catch (Exception e) {
                MyLogger.get().log(Level.FINER, e.toString());
            }
        } else {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                return bufferedReader.readLine();
            } catch (Exception e) {
                MyLogger.get().log(Level.FINER, e.toString());
            }
        }
        return null;
    }

    private static final String localFilePath = initLocalFilePath();
}
