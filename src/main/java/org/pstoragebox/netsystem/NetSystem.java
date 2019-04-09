package org.pstoragebox.netsystem;

import org.pstoragebox.netsystem.Tcp.TcpService;

public class NetSystem {
    // 获取在线主机的ID
    public static String[] getOnlineId(){
        return TcpService.getOnlineClientList().toArray(String[]::new);
    }

    public static Integer getOnlineNodes(){
        return TcpService.getOnlineClientsCount();
    }

    // 上传文件 返回结果
    // 目标主机ID，byte数组，新文件路径
    public static boolean uploadFile(String aimID,byte[] data,String filePath){
        Boolean block = TcpService.sendBlockTo(aimID, data, filePath);
        updateData(getLatestData());
        return block;
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

    // 上传文件完毕之后给网络内所有在线节点更新最新的文件列表
    // 传输的数据是 Map<String,LogicalFile> 对象序列化之后的数据
    public static void updateData(byte[] data){
        for (var aimID : getOnlineId()){
            // aimID : FileSystem obj updateData(data)
        }
    }

    private static byte[] getLatestData(){
        String oneId = getOnlineId()[(int)(Math.random() * getOnlineId().length)];
        byte[] data = null;
        // data = aimID.FileSystem.getMyData();
        return data;
    }
}
