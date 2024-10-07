package server.domain.orderRoom.model;

import java.io.Serializable;

public enum OrderRoomType implements Serializable {
    BY_PRICE("BY_PRICE"),
    BY_MENU("BY_MENU");

    private final String name;

    OrderRoomType(String name) {
        this.name = name;
    }

    public static OrderRoomType fromName(String type) {
        return OrderRoomType.valueOf(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
