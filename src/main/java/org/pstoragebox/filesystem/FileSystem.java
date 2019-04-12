package org.pstoragebox.filesystem;

import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.FormatSystemPrint;
import org.pstoragebox.tools.MyLogger;
import org.pstoragebox.tools.ObjSerialize;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.*;

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

    public void uploadCommand(final String fileName,final String filePath) throws IOException {
        logicalFileList.put(fileName, new LogicalFile(myId, fileName, backupNum, FileStream.readFile(filePath)));
        //getMyData();
    }

    public void downloadCommand(final String fileName,final String filePath) throws IOException {
        if (!logicalFileList.containsKey(fileName)) throw new IOException("File not exist");
        else FileStream.saveFile(filePath, logicalFileList.get(fileName).downloadFile());
    }

    @Deprecated
    public byte[] getMyData() {
        return writeInto(logicalFileList);
    }

    public String getLogicalFileList(){
        try {
            return ObjSerialize.serializeToString(logicalFileList);
        } catch (IOException e) {
            MyLogger.get().log(Level.SEVERE,e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void setLogicalFileList(final String serializedObj){
        try {
            logicalFileList.putAll((Map<String, LogicalFile>) ObjSerialize.deserializeToObject(serializedObj));
            printInfo("收到新的LogicalFileList Info并已合并成功");
            printInfo("文件系统已更新 请输入ls查看");
        } catch (IOException | ClassNotFoundException e) {
            printError("反序列化失败");
            MyLogger.get().log(Level.SEVERE,e.getMessage());
            e.printStackTrace();
        }
    }

    @Deprecated
    public void updateData(byte[] data) {
        var d = restore(data);
        if (d!=null) logicalFileList.putAll(d);
        else MyLogger.get().log(Level.SEVERE,"数据为空");
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
