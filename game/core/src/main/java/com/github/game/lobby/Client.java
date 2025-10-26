package com.github.game.lobby;

import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID = 2L;
    private int id;
    private String name;
    private String ip;
    private boolean isReady;

    public Client(int id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.isReady = (id == 0); // Luôn là chưa sẵn sàng khi mới tạo/ Host luôn sẵn sàng
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
    @Override
    public String toString() {
        String status = isReady ? "San sang" : "Chua san sang";
        return String.format("[ID: %d] %s (%s) - %s", id, name, ip, status);
    }
}
