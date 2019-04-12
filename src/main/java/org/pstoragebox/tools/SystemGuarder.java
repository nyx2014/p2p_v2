package org.pstoragebox.tools;

import org.pstoragebox.system.PStorageBox;

import java.io.*;
import java.util.logging.Level;

public class SystemGuarder{

    public static void saveSystem() {
        var file = new File(configFilePath);
        try {
            if (file.exists()) if (!file.delete()) throw new IOException();
            FileStream.saveFile(configFilePath,PStorageBox.getFileSystem().getMyData());
        } catch (Exception e) {
            MyLogger.get().log(Level.SEVERE, e.toString());
        }
    }

    public static boolean recoverSystem() {
        var file = new File(configFilePath);
        if (!file.exists()) {
            MyLogger.get().log(Level.SEVERE, "系统备份不存在");
            return false;
        }
        try {
            PStorageBox.getFileSystem()
                    .updateData(FileStream.readFile(configFilePath));
        } catch (Exception e) {
            MyLogger.get().log(Level.SEVERE, e.toString());
            return false;
        }
        return true;
    }

    private static final String configFilePath = ConfigPathTool.get("normalConfig");
}
