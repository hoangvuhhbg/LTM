package com.github.game.lobby;

import com.github.game.network.Packet;
import com.github.game.network.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class LobbyClient {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private int myId = -1;
    private boolean isHost = false;
    private boolean isReady = false;

    private ArrayList<Client> currentLobbyState = new ArrayList<>();

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            String name = "";
            while (true) {
                System.out.print("\nAn (1) de Tao Phong, (2) de Vao Phong: ");
                String choice = scanner.nextLine();

                // Nhap ten
                System.out.print("Nhap ten cua ban: ");
                name = scanner.nextLine();


                String serverIp;
                if (choice.equals("1")) {
                    serverIp = "127.0.0.1";
                    System.out.println("Dang tao phong... (Hay chay LobbyServer.java truoc)");
                    System.out.println("IP Radmin VPN cua ban la IP phong cho nguoi khac.");
                } else if (choice.equals("2")) {
                    System.out.print("Nhap IP Radmin VPN cua Host: ");
                    serverIp = scanner.nextLine();
                } else {
                    System.out.println("Lua chon khong hop le.");
                    continue;
                }

                try {
                    System.out.println("Dang thu ket noi den " + serverIp + "...");
                    socket = new Socket(serverIp, 57666);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());

                    System.out.println("Da ket noi toi server... Dang gui yeu cau vao phong...");
                    break;

                } catch (Exception e) {
                    System.err.println("\n*** KHONG THE KET NOI: " + e.getMessage());
                    System.err.println("Vui long kiem tra lai IP hoac dam bao server da chay.");
                }
            }

            // Gửi gói tin join
            Packet joinRequest = new Packet(PacketType.CLIENT_REQUEST_JOIN, name);
            sendPacket(joinRequest);

            new Thread(this::listenFromServer).start();

            // Vòng lặp xử lý lệnh trong phòng
            System.out.println("Go 'ready' de san sang/huy, 'start' de bat dau (neu la Host)");
            while (true) {
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("ready")) {
                    isReady = !isReady;
                    Packet statusPacket = new Packet(PacketType.CLIENT_UPDATE_STATUS, isReady);
                    sendPacket(statusPacket);
                    System.out.println("Ban da cap nhat trang thai: " + (isReady ? "San sang" : "Chua san sang"));

                } else if (input.equalsIgnoreCase("start")) {
                    if (isHost) {
                        Packet startRequest = new Packet(PacketType.CLIENT_REQUEST_START, null);
                        sendPacket(startRequest);
                    } else {
                        System.out.println("Ban khong phai Host, khong thể bat dau game.");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Loi Client: " + e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
                System.out.println("Da ngat ket noi.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Gửi gói tin lên server
    private void sendPacket(Packet packet) {
        try {
            if (oos != null) {
                oos.writeObject(packet);
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println("Loi khi gui goi tin: " + e.getMessage());
        }
    }

    private void listenFromServer() {
        try {
            while (true) {
                Packet packet = (Packet) ois.readObject();

                switch (packet.getType()) {
                    case SERVER_ACCEPT_JOIN:
                        this.myId = (Integer) packet.getData();
                        if (this.myId == 0) {
                            this.isHost = true;
                            System.out.println("Ban da vao phong voi tu cach la HOST!");
                        } else {
                            System.out.println("Ban da vao phong voi ID: " + this.myId);
                        }
                        break;
                    case SERVER_UPDATE_LOBBY:
                        this.currentLobbyState = (ArrayList<Client>) packet.getData();
                        System.out.println("\n--- CAP NHAT PHONG CHO ("+ this.currentLobbyState.size() +" nguoi) ---");
                        for (Client client : this.currentLobbyState) {
                            System.out.println(client.toString() + (client.getId() == myId ? " (La ban)" : ""));
                        }
                        System.out.println("-------------------------------------");
                        System.out.print("Lenh (ready/start): ");
                        break;

                    case SERVER_START_GAME:
                        System.out.println("\n!!! GAME BAT DAU !!!");
                        // TODO: Chuyển màn hình sang game chính
                        return;
                    case SERVER_ERROR:
                        System.err.println("\n[LOI TU SERVER]: " + packet.getData().toString());
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Server da ngat ket noi.");
        }
    }
    public static void main(String[] args) {
        new LobbyClient().start();
    }
}