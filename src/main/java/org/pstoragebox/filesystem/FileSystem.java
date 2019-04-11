package org.pstoragebox.filesystem;

import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.tools.FileStream;

import java.io.*;
import java.util.*;

public class FileSystem{

    public FileSystem(String rootCatalog, String myId) {
        this.rootCatalog = rootCatalog;
        this.myId = myId;
        logicalFileList = new HashMap<>();
    }

    public String[] lsCommand() {
        Set<String> keys = logicalFileList.keySet();
        Iterator<String> iterator1 = keys.iterator();
        String[] fileName = new String[keys.size()];
        int i = 0;
        while (iterator1.hasNext()) {
            fileName[i++] = iterator1.next();
        }
        return fileName;
    }

    public void uploadCommand(String fileName, String filePath) throws IOException {
        logicalFileList.put(fileName, new LogicalFile(myId, fileName, backupNum, FileStream.readFile(filePath)));
        //getMyData();
    }

    public void downloadCommand(String fileName, String filePath) throws IOException {
        FileStream.saveFile(filePath, logicalFileList.get(fileName).downloadFile());
    }

    private void sentNewData() {
        byte[] data = writeInto(logicalFileList);
//        NetSystem.updateData(data);
    }

    public byte[] getMyData() {
        return writeInto(logicalFileList);
    }

    public void updateData(byte[] data) {
        logicalFileList = restore(data);
    }

    /**
     * 把对象转变成二进制
     *
     * @param obj 待转换的对象
     * @return 返回二进制数组
     */
    private byte[] writeInto(Map<String, LogicalFile> obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            //读取对象并转换成二进制数据
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {

        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 把二进制数组的数据转回对象
     *
     * @param b
     * @return
     */
    private Map<String, LogicalFile> restore(byte[] b) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            //读取二进制数据并转换成对象
            bis = new ByteArrayInputStream(b);
            ois = new ObjectInputStream(bis);
            return (Map<String, LogicalFile>) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String rootCatalog;
    private String myId;
    private Map<String, LogicalFile> logicalFileList;

    private final static int backupNum = 2;
}
