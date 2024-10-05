package server.domain.orderRoom.model;

public enum OrderRoomStatus {
    ACTIVE("ACTIVE"),
    CANCEL("CANCEL"),
    COMPLETE("COMPLETE");

    private final String name;

    OrderRoomStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
