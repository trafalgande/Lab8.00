import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BdConnect extends Thread{
    String mess = null;
    String priznak;
    String log;
    String pass;
    DatagramSocket socket;
    InetAddress address;
    int port;

    public BdConnect(String priznak, String log, String pass, DatagramSocket socket, InetAddress address, int port){
        this.priznak = priznak;
        this.log = log;
        this.pass = pass;
        this.socket = socket;
        this.address = address;
        this.port = port;
    }

    static final String DB_URL = "jdbc:postgresql://localhost:5432/studs"; //localhost на pg
    static final String USER = "s264476";
    static final String PASS = "sny683";

    static Connection connection;
    static Statement statement;

    private static byte[] recieveData = new byte[256];

    public void connectBd(){ //Присоединились к бд
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            mess = "You successfully connected to database now";
        } else {
            mess = "Failed to make connection to database";
        }
//        recieveData = mess.getBytes();
//        DatagramPacket packet = new DatagramPacket(recieveData, recieveData.length, address, port);
//        try {
//            socket.send(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (priznak.equals("2")){
            auth(log, pass);
        }
        if (priznak.equals("1")){
            try {
                reg(log);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }


    }
    public void auth(String log, String pass){
        PasswordSha256 passwordSha256 = new PasswordSha256();
        String hashPas = null;
        try {
            hashPas = passwordSha256.hashing(pass.trim());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String findPass = null, findLog = null;
        try {
            ResultSet r = statement.executeQuery("SELECT login, pswd FROM authuser WHERE (login LIKE '" + log + "') and (pswd like '" + hashPas + "')");
            while (r.next()) {
                findLog = r.getString("login");
                findPass = r.getString("pswd");
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (findLog != null && findPass != null) {
            mess = "Authorization successful";
        } else {
            mess = "Try again. Check your data.";
        }
        recieveData = mess.getBytes();
        DatagramPacket packet = new DatagramPacket(recieveData, recieveData.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reg(String log) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(4);
        PasswordSha256 passwordSha256 = new PasswordSha256();
        String hashPas = passwordSha256.hashing(password);
        System.out.println(password);
            try {
                statement.execute("INSERT INTO AuthUser (login, pswd) VALUES ('" + log + "','" + hashPas + "')");
                mess = "You are added";
            } catch (SQLException ex){
                mess = "Already exists. Write a new login";
            }

        //String email = "feys-00@mail.ru";
        //EmailSender emailSender = new EmailSender();
        //emailSender.send(password, email);

        recieveData = mess.getBytes();
        DatagramPacket packet = new DatagramPacket(recieveData, recieveData.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
