package org.pstoragebox.netsystem;

import org.pstoragebox.netsystem.Tcp.TcpService;

public class NetSystem {
    // 获取在线主机的ID
    public static String[] getOnlineId(){
        return TcpService.getOnlineClientList().toArray(String[]::new);
    }

    // 上传文件 返回结果
    // 目标主机ID，byte数组，新文件路径
    public static boolean uploadFile(String aimID,byte[] data,String filePath){
        return TcpService.sendBlockTo(aimID,data,filePath);
    }

    // 下载文件 返回块
    // 目标主机ID，文件路径
    public static byte[] downloadFile(String aimID,String filePath){

        return TcpService.requestBlockTo(aimID, filePath);

//        byte[] data = FileStream.readFileBlockFromRealSystem(filePath);
//        return data;
    }

    // 加入网络
    public static boolean joinNet(){
        boolean status = false;

        return status;
    }

    // 离开网络
    public static boolean leaveNet(){
        boolean status = false;

        return status;
    }
}
