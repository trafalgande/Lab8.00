import com.fasterxml.jackson.databind.ObjectMapper;
import sample.Creature;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ComandsPars {
    private static Statement statement;
    private static String log;

    DatagramSocket socket;
    InetAddress address;
    int port;
    private static byte[] recieveData = new byte[256];

    public ComandsPars(Statement statement, String log, DatagramSocket socket, InetAddress address, int port) {
        this.statement = statement;
        this.log = log;
        this.socket = socket;
        this.address = address;
        this.port = port;
    }

    public void doCom(String comanda) throws IOException {
        String mess = new String();
        String id = null;
        ResultSet r;
        System.out.println(log);
        try {
            r = statement.executeQuery("SELECT id FROM authuser WHERE (login LIKE '" + log + "')");
            while (r.next()) {
                id = r.getString("id");
            }
            System.out.println(id);
            r.close();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        //insert{key}{json}
        //add_if_min{json}
        //show
        //insert{key}{"name" : "1111","status": "EAT","location": "ROOF"}
        String comParsed[] = comanda.split(" ");
        String com = comParsed[0];
        String key = comParsed[1];

        switch (com) {
            case "insert":
                try {
                    Creature creature = new Creature(comParsed[2], comParsed[3], comParsed[4]);
                    statement.execute("INSERT INTO creature (id_host, id_key, n_ame, status, locate) VALUES ('" + id + "', '" + key + "', '" + creature.getName() + "','" + creature
                            .getStatus() + "','" + creature.getLocation() + "')");
                    mess = "Done";
                } catch (SQLException e) {
                    mess = "Key already exist. Change key value";
                }
                break;
            case "remove":
                try {
                    statement.executeUpdate("DELETE FROM creature WHERE (id_key LIKE '" + key + "')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mess = "Done";
                break;
            case "show":
                ResultSet r1 = null;
                try {
                    r1 = statement.executeQuery("SELECT * FROM creature");
                    while (r1.next()) {
                        String id_host = r1.getString("id_host");
                        String id_key = r1.getString("id_key");
                        String n_ame = r1.getString("n_ame");
                        String status = r1.getString("status");
                        String locate = r1.getString("locate");
                        mess = mess + "\n" + "id: " + id_host + " key: " + id_key + " Value:" + n_ame + ", " + status + ", " + locate;
                    }
                    r1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "info":
                mess = "Date of creating: 25.04.19" + "\n" + "Type: ConcurrentSkipListMap";
                ResultSet r2 = null;
                try {
                    r2 = statement.executeQuery("select count(*) from authuser");
                    while (r2.next()) {
                        int count = r2.getInt(1);
                        mess = mess + "\n" + "Amount of users: " + count;
                    }
                    r2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "x":
                System.exit(10);
                break;
            default:
                mess = "Try again. Check";
                break;
        }
        recieveData = mess.getBytes();
        DatagramPacket packet = new DatagramPacket(recieveData, recieveData.length, address, port);
        try {
            socket.send(packet);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}

