package org.pstoragebox.tools;

import java.io.*;
import java.util.logging.Level;

public class FileStream {

    public static void writeFileBlockToRealSystem(String filePath,byte[] data){
        File file = new File(LocalPathManager.getLocalFilePath()+"\\"+filePath);
        if (file.exists() && file.isFile()){
            MyLogger.getMyLogger().log(Level.FINER,"文件块重名，无法保存文件");
            return;
        }
        try {
            if (file.createNewFile()){
                DataOutputStream out = new DataOutputStream(new FileOutputStream(LocalPathManager.getLocalFilePath()+"\\"+filePath, true));
                out.write(data);
                out.close();
            }
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER,e.toString());
        }
    }

    public static void saveFile(String filePath,byte[] data) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()){
            MyLogger.getMyLogger().log(Level.FINER,"文件块重名，无法保存文件");
            return;
        }
        try {
            if (file.createNewFile()){
                DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath));
                out.write(data);
                out.close();
            }
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER,e.toString());
        }
    }


    public static byte[] readFileBlockFromRealSystem(String filePath){
        byte[] data= null;
        filePath = LocalPathManager.getLocalFilePath()+"\\"+filePath;
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            MyLogger.getMyLogger().log(Level.FINER,"file too big...");
            return data;
        }
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            data = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < data.length
                    && (numRead = fi.read(data, offset, data.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != data.length) {
                MyLogger.getMyLogger().log(Level.FINER,"Could not completely read file "
                        + file.getName());
            }
            fi.close();
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER,e.toString());
        }

        return data;
    }

    public static byte[] readFile(String filePath){
        byte[] data= null;
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            MyLogger.getMyLogger().log(Level.FINER,"file too big...");
            return data;
        }
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            data = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < data.length
                    && (numRead = fi.read(data, offset, data.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != data.length) {
                MyLogger.getMyLogger().log(Level.FINER,"Could not completely read file "
                        + file.getName());
            }
            fi.close();
        } catch (Exception e) {
            MyLogger.getMyLogger().log(Level.FINER,e.toString());
        }

        return data;
    }
}
