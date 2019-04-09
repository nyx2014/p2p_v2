package org.pstoragebox.filesystem;

import org.pstoragebox.tools.FileStream;

import java.util.*;

public class FileSystem {

    public FileSystem(String rootCatalog, String myId) {
        this.rootCatalog = rootCatalog;
        this.myId = myId;
        logicalFileList = new HashMap<>();
    }

    public String[] lsCommand(){
        Set<String> keys=logicalFileList.keySet();
        Iterator<String> iterator1=keys.iterator();
        String[] fileName = new String[keys.size()];
        int i = 0;
        while (iterator1.hasNext()){
            fileName[i++] = iterator1.next();
        }
        return fileName;
    }

    public void uploadCommand(String fileName,String filePath){
        byte[] data = FileStream.readFile(filePath);
        LogicalFile lFile = new LogicalFile(myId,fileName,backupNum,data);
        logicalFileList.put(fileName,lFile);
    }

    public void downloadCommand(String fileName,String filePath){
        FileStream.saveFile(filePath,logicalFileList.get(fileName).downloadFile());
    }

    private String rootCatalog;
    private String myId;
    private Map<String,LogicalFile> logicalFileList;

    private final static int backupNum = 2;
}
