package com.github.game.network;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    private PacketType type;
    private Object data;

    public Packet(PacketType type, Object data) {
        this.type = type;
        this.data = data;
    }
    public PacketType getType() {
        return type;
    }
    public Object getData() {
        return data;
    }
}
