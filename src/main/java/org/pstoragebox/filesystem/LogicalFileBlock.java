package org.pstoragebox.filesystem;

import org.pstoragebox.netsystem.NetSystem;
import org.pstoragebox.tools.FileStream;
import org.pstoragebox.tools.MyLogger;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.pstoragebox.tools.FormatSystemPrint.printError;
import static org.pstoragebox.tools.FormatSystemPrint.printInfo;

public class LogicalFileBlock implements Serializable {

    private Map<String, String> blockLocations;

    LogicalFileBlock(final String myId, final String filePath, final byte[] data) {
        blockLocations = new HashMap<>();
        try {
            FileStream.writeFileBlockToRealSystem(filePath, data);
        } catch (IOException e) {
            printError("Failed write block to disk");
            return;
        }
        blockLocations.put(myId, filePath);
    }

    void addABackup() {
        printInfo("Start Add backup");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final var nodeList = NetSystem.getOnlineId();
        if (nodeList.length <= blockLocations.size()) {
            MyLogger.get().log(Level.WARNING, "备份创建失败，已存在备份数目大于等于节点数目");
            return;
        }
        var randomNode = nodeList[(int) (Math.random() * nodeList.length)];
        while (blockLocations.containsKey(randomNode))
            randomNode = nodeList[(int) (Math.random() * nodeList.length)];

        final var keys = blockLocations.keySet().toArray(new String[0]);
        final var randomKey = keys[(int) (Math.random() * keys.length)];
        final var value = blockLocations.get(randomKey);
        byte[] data;
        try {
            data = NetSystem.networkGetBlock(randomKey, value);
            printInfo("download first");
            NetSystem.networkSendBlock(randomNode, data, value);
            printInfo("upload next");
            blockLocations.put(randomNode, value);
            printInfo("Add backup success");
        } catch (IOException e) {
            printError("Add backup failed: " + e.getMessage());
        }
    }

    public void deleteBackUpbyId(String id) {

    }

    byte[] downloadBlock() {

        printInfo("Start download Block");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final var keys = blockLocations.keySet().toArray(new String[0]);
        final var randomKey = keys[(int) (Math.random() * keys.length)];
        try {
            return NetSystem.networkGetBlock(randomKey, blockLocations.get(randomKey));
        } catch (IOException e) {
            printError("download block failed");
            return new byte[0];
        }
    }
}
