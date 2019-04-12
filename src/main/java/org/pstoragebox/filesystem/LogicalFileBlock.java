package org.pstoragebox.filesystem;

import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.MyLogger;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.*;

public class LogicalFileBlock implements Serializable {

    LogicalFileBlock(String myId,String filePath,byte[] data) {
        blockLocations = new HashMap<>();
        try {
            FileStream.writeFileBlockToRealSystem(filePath,data);
        } catch (IOException e) {
            printError("Failed write block to disk");
            return;
        }
        blockLocations.put(myId,filePath);
    }

    void addABackup(){
        String[] nodeList = NetSystem.getOnlineId();
        if (nodeList.length <= blockLocations.size()){
            MyLogger.get().log(Level.FINER,"备份创建失败，已存在备份数目大于等于节点数目");
            return;
        }
        String randomNode = nodeList[(int)(Math.random() * nodeList.length)];
        while (blockLocations.get(randomNode) != null)
            randomNode = nodeList[(int)(Math.random() * nodeList.length)];

        String[] keys = blockLocations.keySet().toArray(new String[0]);
        String randomKey = keys[(int)(Math.random() * keys.length)];
        String value = blockLocations.get(randomKey);
        byte[] data = new byte[0];
        try {
            data = NetSystem.downloadFile(randomKey,value);
        } catch (IOException e) {
//            e.printStackTrace();
            printError("download failed");
        }

        if (NetSystem.uploadFile(randomNode,data,value))
            printInfo("upload success");
//            MyLogger.get().log(Level.INFO,"成功");
        else
            printError("upload failed");
//            MyLogger.get().log(Level.WARNING,"网络模块报告失败");
        blockLocations.put(randomNode,value);
    }

    public void deleteBackUpbyId(String id){

    }

    byte[] downloadBlock(){
        String[] keys = blockLocations.keySet().toArray(new String[0]);
        String randomKey = keys[(int)(Math.random() * keys.length)];
        String value = blockLocations.get(randomKey);
        byte[] data = new byte[0];
        try {
            data = NetSystem.downloadFile(randomKey,value);
        } catch (IOException e) {
            printError("download failed");
        }
        return data;
    }


    private Map<String,String> blockLocations;
}
