package org.pstoragebox.netsystem;

import org.pstoragebox.netsystem.Tcp.TcpService;
import org.pstoragebox.tools.AutoIdGenerator;
import org.pstoragebox.tools.FileStream;

import java.io.IOException;
import java.util.HashSet;

public class NetSystem {
    // 获取在线主机的ID
    public static String[] getOnlineId() {
        var list = new HashSet<>(TcpService.getOnlineClientList());
        String id = null;
        try {
            id = AutoIdGenerator.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.add(id);
        return list.toArray(String[]::new);
    }

    // 上传文件 返回结果
    // 目标主机ID，byte数组，新文件路径
    public static void networkSendBlock(String aimID, byte[] data, String filePath) throws IOException {
        if(AutoIdGenerator.getId().equals(aimID))
            FileStream.writeFileBlockToRealSystem(filePath, data);
        else TcpService.sendBlockTo(aimID, data, filePath);
//        return TcpService.sendBlockTo(aimID, data, filePath);
    }

    // 下载文件 返回块
    // 目标主机ID，文件路径
    public static byte[] networkGetBlock(String aimID, String filePath) throws IOException {
//        return (AutoIdGenerator.getId().equals(aimID)) ? FileStream.readFileBlockFromRealSystem(filePath)
//                : TcpService.requestBlockTo(aimID, filePath);
        return FileStream.readFileBlockFromRealSystem(filePath);
    }

//    // 加入网络
//    public static boolean joinNet() {
//        boolean status = false;
//
//        return status;
//    }
//
//    // 离开网络
//    public static boolean leaveNet() {
//        var status = false;
//
//        return status;
//    }

}
