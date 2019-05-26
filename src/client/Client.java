package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    private static InetAddress address;
    private static int port = 9941;
    private static DatagramSocket socket;

    public static void connect() {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> socket.close()));
    }

    public String doCom(String priznak, String inlog, String inpass) {
        String logPass;
        String regOrAuth;
        String com;
        regOrAuth = priznak.trim();

        byte[] sendData;
        byte[] receiveData = new byte[1024];
        // если x, то выход. если 1, то рег. если 2, то авторизац, если другое, то это команда
        if (regOrAuth.equals("x")) {
            try {
                sendData = regOrAuth.getBytes();
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
                socket.send(packet);   //отправили признак команды+команду
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (regOrAuth.equals("2")) {        //авторизация
            String log = inlog.trim();
            String pass = inpass.trim();
            try {
                logPass = regOrAuth + " " + log + " " + pass;
                sendData = logPass.getBytes();
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
                socket.send(packet);   //отправили признак авторизации+логин+пароль

                packet = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(packet); //ждем ответ
                String mess = new String(packet.getData());
                System.out.println(mess.trim());
                if (mess.contains("successful")) {
                    regOrAuth = "3";
                    return "Auth done!";
                } else {
                    return mess.trim();
                }
            } catch (IOException ex) {
                System.out.println("Problem with server...");
            }
        }
        if (regOrAuth.equals("1")) {     //регистрация
            String log = inlog.trim();
            try {
                logPass = regOrAuth + " " + log;
                sendData = logPass.getBytes();
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
                socket.send(packet);   //отправили признак регистрации+логин

                packet = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(packet); //ждем ответ
                String mess = new String(packet.getData());
                if (mess.contains("added")) {
                    regOrAuth = "3";
                    return "Reg done!";
                } else {
                    return mess.trim();
                }
            } catch (IOException ex) {
                System.out.println("Problem with server...");
            }
        }
        //insert{key}{"name" : "1111","status": "EAT","location": "ROOF"}
        if (!regOrAuth.equals("1") && !regOrAuth.equals("2") && !regOrAuth.equals("x")) { //если не 1, не 2, и не х, то повторный ввод команды
            com = priznak;
            com = "3 " + com.trim();
            try {
                sendData = com.getBytes();
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
                socket.send(packet);   //отправили признак команды+команду

                packet = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(packet); //ждем ответ в виде успешного выполнения или ошибки
                String mess = new String(packet.getData());
                System.out.println(mess.trim());
                regOrAuth = "3";
            } catch (IOException ex) {
                System.out.println("Problem with server...");
            }
            return "Done!";
        }
        return "Check it";
    }
}

