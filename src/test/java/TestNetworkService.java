import org.pstoragebox.netsystem.NetworkService;
import org.pstoragebox.netsystem.Tcp.TcpService;

public class TestNetworkService {

    public static void main(String[] args) {
        NetworkService.startNetworkService();
//        new Thread(() -> {
//            try {
//                Thread.sleep(6000);
//                TcpService.sendBlockTo("123456","testmonkdata".getBytes(),"whatever");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(6000);
                TcpService.requestBlockTo("123456", "whatever");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(20000);
            NetworkService.stopNetworkService();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
