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

    public void broadcastLobbyState() throws IOException {
        ArrayList<Client> clients = new ArrayList<>();
        synchronized (connectedClients) {
            for (ClientHandler clientHandler : connectedClients){
                if(clientHandler.getClient() != null) {
                    clients.add(clientHandler.getClient());
                }
            }
        }

        System.out.println("Broadcasting lobby state... So luong: " + clients.size());

        Packet updatePacket = new Packet(PacketType.SERVER_UPDATE_LOBBY, clients);
        synchronized (connectedClients) {
            for (ClientHandler clientHandler : connectedClients){
                try {
                    clientHandler.sendPacket(updatePacket);
                } catch (IOException e) {
                    System.err.println("Loi khi broadcast cho client " + clientHandler.getClientID());
                }
            }
        }
    }

    public boolean areAllClientsReady(){
        if (connectedClients.isEmpty()){
            return false;
        }
        synchronized (connectedClients){
            for (ClientHandler clientHandler : connectedClients){
                if (!clientHandler.getClient().isReady() || clientHandler.getClient() == null){
                    return false;
                }
            }
        }
        return true;
    }

    public void handleStartGameRequest(ClientHandler clientHandler) throws IOException {
        if (clientHandler.getClientID() != HOST_ID){
            clientHandler.sendPacket(new Packet(PacketType.SERVER_ERROR, "Khong phai Host de bat dau game"));
            return;
        }
        if (areAllClientsReady()){
            System.out.println("Host yeu cau bat dau... Tat ca da san sang!");

            // Gửi tín hiệu bắt đầu cho TẤT CẢ
            Packet startPacket = new Packet(PacketType.SERVER_START_GAME, "START");
            for (ClientHandler handler : connectedClients) {
                handler.sendPacket(startPacket);
            }

            // TODO: Gọi để chạy game
        } else {
            System.out.println("Host yeu cau bat dau... nhung co nguoi chua san sang.");
            clientHandler.sendPacket(new Packet(PacketType.SERVER_ERROR, "Chua the bat dau, co nguoi chua san sang!"));
        }
    }

    public boolean isNameTaken(String name) {
        synchronized (connectedClients) {
            for (ClientHandler handler : connectedClients) {
                // Kiểm tra xem tên đã tồn tại và khác null chưa
                if (handler.getClient() != null && handler.getClient().getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeClient(ClientHandler clientHandler) throws IOException {
        connectedClients.remove(clientHandler);
        if (clientHandler.getClient() != null) {
            System.out.println(clientHandler.getClient().getName() + " da roi phong.");
            broadcastLobbyState();
        }
    }

    public static void main(String[] args) {
        new LobbyServer().startServer();
    }
}
