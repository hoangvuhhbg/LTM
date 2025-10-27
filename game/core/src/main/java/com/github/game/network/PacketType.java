package com.github.game.network;

public enum PacketType {
    // Yêu cầu vào phòng, cập nhật trạng thái client, host bắt đầu game
    CLIENT_REQUEST_JOIN,
    CLIENT_UPDATE_STATUS,
    CLIENT_REQUEST_START,

    // Server chấp nhận vao, cập nhật phòng, bắt đầu game, server lỗi
    SERVER_ACCEPT_JOIN,
    SERVER_UPDATE_LOBBY,
    SERVER_START_GAME,
    SERVER_ERROR
}
