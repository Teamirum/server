package server.domain.order.model;

public enum TogetherOrderStatus {

    WAIT("WAIT"),
    CANCEL("CANCEL"),
    COMPLETE("COMPLETE");

    private final String name;

    TogetherOrderStatus(String name) {
        this.name = name;
    }

    public static TogetherOrderStatus fromName(String type) {
        return TogetherOrderStatus.valueOf(type);
    }

    @Override
    public String toString() {
        return name;
    }

}
