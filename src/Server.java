import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {

    private static DatagramSocket socket;
    private static boolean running;
    private  static String login;
    private static int port = 9941;

    public static void main(String[] args) {
        String priznak;
        String priznakCom;
        String com;
        String log;
        String pass;
        running = true;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> socket.close()));

        while (running) {
            byte[] recieveData = new byte[256];
            DatagramPacket packet
                    = new DatagramPacket(recieveData, recieveData.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress(); //нужно только для отправки ответа клиенту
            int port = packet.getPort();
            String received
                    = new String(packet.getData());
            if (received.trim().equals("x")) {
                running = false;
                socket.close();
                System.exit(0);
            }
            String[] rec = received.split(" "); //в случае регистра/авторизации
            priznak = rec[0].trim();
            log = rec[1].trim();
            //System.out.println(priznak + " " + log);
            if (priznak.equals("2")) { //авторизируем юзера
                login = log;
                pass = rec[2];
                new BdConnect(priznak, login, pass, socket, address, port).connectBd();
            }
            if (priznak.equals("1")) { //регистрируем юзера
                login = log;
                pass = "";
                new BdConnect(priznak, login, pass, socket, address, port).connectBd();
            }
            String[] recCom = received.split(" ", 2);
            priznakCom = recCom[0]; //тут будет 3 если получили команду
            com = recCom[1].trim(); //тут будет вся команда
            if (priznakCom.equals("3")) {
                try {
                    new ComandsPars(BdConnect.statement, login, socket, address, port).doCom(com);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        socket.close();
    }
}
