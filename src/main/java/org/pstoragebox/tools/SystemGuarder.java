package org.pstoragebox.tools;

import org.pstoragebox.filesystem.FileSystem;
import org.pstoragebox.system.PStorageBox;

import java.io.*;
import java.util.logging.Level;

public class SystemGuarder{

    public static void saveSystem() {
        File file = new File(configFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileStream.saveFile(configFilePath,PStorageBox.getFileSystem().getMyData());
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER, e.toString());
        }
    }

    public static boolean recoverSystem() {
        File file = new File(configFilePath);
        if (!file.exists()) {
            MyLogger.getMyLogger().log(Level.FINER, "系统备份不存在");
            return false;
        }
        try {
            byte[] data = FileStream.readFile(configFilePath);
            PStorageBox.getFileSystem().updateData(data);
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER, e.toString());
            return false;
        }
        return true;
    }

    private static final String configFilePath = ConfigPathTool.get("normalConfig");
}
