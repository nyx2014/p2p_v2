package org.pstoragebox.filesystem;

import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LogicalFileBlock {

    LogicalFileBlock(String myId,String filePath,byte[] data) {
        blockLocations = new HashMap<>();
        FileStream.writeFileBlockToRealSystem(filePath,data);
        blockLocations.put(myId,filePath);
    }

    public void addABackup(){
        String[] nodeList = NetSystem.getOnlineId();
        if (nodeList.length <= blockLocations.size()){
            MyLogger.getMyLogger().log(Level.FINER,"备份创建失败，已存在备份数目大于等于节点数目");
            return;
        }
        String randomNode = nodeList[(int)(Math.random() * nodeList.length)];
        while (blockLocations.get(randomNode) != null)
            randomNode = nodeList[(int)(Math.random() * nodeList.length)];

        String[] keys = blockLocations.keySet().toArray(new String[0]);
        String randomKey = keys[(int)(Math.random() * keys.length)];
        String value = blockLocations.get(randomKey);
        byte[] data = NetSystem.downloadFile(randomKey,value);

        if (NetSystem.uploadFile(randomNode,data,value))
            MyLogger.getMyLogger().log(Level.INFO,"成功");
        else
            MyLogger.getMyLogger().log(Level.WARNING,"网络模块报告失败");
        blockLocations.put(randomNode,value);
    }

    public void deleteBackUpbyId(String id){

    }

    byte[] downloadBlock(){
        String[] keys = blockLocations.keySet().toArray(new String[0]);
        String randomKey = keys[(int)(Math.random() * keys.length)];
        String value = blockLocations.get(randomKey);
        byte[] data = NetSystem.downloadFile(randomKey,value);
        return data;
    }


    private Map<String,String> blockLocations;
}
