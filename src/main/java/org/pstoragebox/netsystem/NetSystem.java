package org.pstoragebox.netsystem;

import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.system.PStorageBox;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.FileStream;

import java.io.IOException;

public class NetSystem {
    // 获取在线主机的ID
    public static String[] getOnlineId() {
        return TcpService.getOnlineClientList().toArray(String[]::new);
    }

    public static Integer getOnlineNodes() {
        return TcpService.getOnlineClientsCount();
    }

    // 上传文件 返回结果
    // 目标主机ID，byte数组，新文件路径
    public static boolean uploadFile(String aimID, byte[] data, String filePath) {
        return TcpService.sendBlockTo(aimID, data, filePath);
    }

    // 下载文件 返回块
    // 目标主机ID，文件路径
    public static byte[] downloadFile(String aimID, String filePath) throws IOException {
        return (AutoIdGenerator.getId().equals(aimID)) ? FileStream.readFileBlockFromRealSystem(filePath)
                : TcpService.requestBlockTo(aimID, filePath);
    }

    // 加入网络
    public static boolean joinNet() {
        boolean status = false;

        return status;
    }

    // 离开网络
    public static boolean leaveNet() {
        var status = false;

        return status;
    }

}
