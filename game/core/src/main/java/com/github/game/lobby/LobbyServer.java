package com.github.game.lobby;

import com.github.game.network.Packet;
import com.github.game.network.PacketType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyServer {
    private static final int PORT = 57666;
    private static final int HOST_ID = 0; // Client đầu tiên (ID 0) là host

    private List<ClientHandler> connectedClients = Collections.synchronizedList(new ArrayList<>());
    private AtomicInteger nextID = new AtomicInteger(0);

    public void startServer() {
        try (ServerSocket socket = new ServerSocket(PORT)){
            String radminIp = socket.getInetAddress().getHostAddress(); // Là IP của máy radmin
            System.out.println("=== Server Lobby da khoi dong ===");
            System.out.println("IP Phong (Radmin VPN): " + radminIp);
            System.out.println("Port: " + PORT);
            System.out.println("Dang cho client ket noi...");

            while (true){
                Socket clientSocket = socket.accept(); // Chấp nhận kết nối từ nguười chơi

                int clientId = nextID.getAndIncrement();
                System.out.println("Client moi ket noi: " + clientSocket.getInetAddress().getHostAddress() + " voi ID = " + clientId);

                // Tạo luồng xử lý cho client này
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId, this);
                connectedClients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LobbyServer().startServer();
    }
}
