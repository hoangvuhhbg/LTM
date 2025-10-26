package com.github.game.lobby;

import com.github.game.network.Packet;
import com.github.game.network.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket connection;
    private int clientID;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Client client;
    private LobbyServer server;

    public ClientHandler(Socket connection, int clientID, LobbyServer server) {
        this.connection = connection;
        this.clientID = clientID;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            while (true) {
                Packet packet = (Packet) input.readObject();

                switch (packet.getType()) {
                    case CLIENT_REQUEST_JOIN:
                        String name = (String) packet.getData();
                        String ip = connection.getInetAddress().getHostAddress();
                        client = new Client(clientID, name, ip);

                        sendPacket(new Packet(PacketType.SERVER_ACCEPT_JOIN, clientID));
                        System.out.println(client + " da vao phong.");
                        break;
                    default:
                        System.out.println("Goi tin khong hop le tu client " + clientID);
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("Client ID " + clientID + " da ngat ket noi.");
        } finally {
            // Đóng tất cả kết nối
            try {
                if (output != null) output.close();
                if (input != null) input.close();
                if (connection != null) connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPacket(Packet packet) throws IOException {
        if (output != null) {
            output.writeObject(packet);
            output.flush();
            output.reset();
        }
    }
}
