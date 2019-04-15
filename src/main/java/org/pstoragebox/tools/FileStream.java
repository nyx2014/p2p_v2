package org.pstoragebox.tools;

import java.io.*;

import static org.pstoragebox.tools.FormatSystemPrint.printInfo;

public class FileStream {

    public static void writeFileBlockToRealSystem(String filePath, byte[] data) throws IOException {
        printInfo("DEBUG: data length to write: " + data.length);
        saveFile(LocalPathManager.getLocalFilePath() + "\\" + filePath, data);
//        var file = new File(LocalPathManager.getLocalFilePath() + "\\" + filePath);
//        if (file.exists() && file.isFile()) throw new IOException("文件块重名，无法保存文件");
//        try {
//            if (file.createNewFile()) {
//                DataOutputStream out = new DataOutputStream(new FileOutputStream(LocalPathManager.getLocalFilePath() + "\\" + filePath, true));
//                byte[] tmp = new byte[LogicalFile.blockSize];
//                System.arraycopy(data, 0, tmp, 0, LogicalFile.blockSize);
//                out.write(tmp);
//                out.close();
//            }
//        } catch (Exception e) {
//            MyLogger.get().log(Level.FINER, e.toString());
//        }
    }

    public static void saveFile(String filePath, byte[] data) throws IOException {
        var file = new File(filePath);
        if (file.exists() && file.isFile()) throw new IOException("文件块重名，无法保存文件");
        if (!file.createNewFile()) return;
        var out = new DataOutputStream(new FileOutputStream(filePath));
        out.write(data);
        out.close();
    }

    public static byte[] readFileBlockFromRealSystem(String filePath) throws IOException {
        return readFile(LocalPathManager.getLocalFilePath() + "\\" + filePath);
    }

    public static byte[] readFile(String filePath) throws IOException {
        var file = new File(filePath);
        final var fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) throw new IOException("file too big");
        var fi = new FileInputStream(file);
        var data = new byte[(int) fileSize];
        var offset = 0;
        int numRead;
        while (offset < data.length
                && (numRead = fi.read(data, offset, data.length - offset)) >= 0) offset += numRead;
        // 确保所有数据均被读取
        if (offset != data.length) throw new IOException("Could not completely read file " + file.getName());
        fi.close();
        return data;
    }
}
