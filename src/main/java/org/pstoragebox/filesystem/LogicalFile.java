package org.pstoragebox.filesystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogicalFile implements Serializable {

    public final static int blockSize = 1048576;
    private String fileName;
    private int fileSize;
    private int backupNum;
    private int blockNum;
    private List<LogicalFileBlock> fileBlockList;

    LogicalFile(final String myId, final String fileName, final int backupNum, final byte[] data) {
        this.fileName = fileName;
        this.fileSize = data.length;
        this.backupNum = backupNum;
        fileBlockList = new ArrayList<>();

        blockNum = data.length / blockSize;
        if (data.length % blockSize != 0)
            blockNum++;

        for (int i = 0; i < blockNum; i++) {
            var tempData = new byte[blockSize];
            for (int j = 0; j < blockSize; j++) {
                if (i * blockSize + j < data.length) tempData[j] = data[i * blockSize + j];
                else tempData[j] = -1;
            }
            final var filePath = myId + "_" + fileName + "_" + i;
            var tempBlock = new LogicalFileBlock(myId, filePath, tempData);
            var lastBackupNum = backupNum - 1;
            while (lastBackupNum > 0) {
                tempBlock.addABackup();
                lastBackupNum--;
            }
            fileBlockList.add(tempBlock);
        }
    }

    byte[] downloadFile() {
        var data = new byte[0];
        for (int i = 0; i < blockNum; i++) {
            var tempData = fileBlockList.get(i).downloadBlock();
            var newData = new byte[data.length + tempData.length];
            System.arraycopy(data, 0, newData, 0, data.length);
            System.arraycopy(tempData, 0, newData, data.length, tempData.length);
            data = newData;
        }
        var result = new byte[fileSize];
        if (data.length > fileSize) System.arraycopy(data, 0, result, 0, fileSize);
        return result;
    }

//    public String getFileName() {
//        return fileName;
//    }
//
//    public int getBlockNum() {
//        return blockNum;
//    }
//
//    public void setBlockNum(int blockNum) {
//        this.blockNum = blockNum;
//    }
}
