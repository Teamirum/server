package server.domain.pay.model;

public enum PayStatus {

    ACCEPT("ACCEPT"),
    WAIT("WAIT"),
    FAIL("FAIL"),
    CANCEL("CANCEL");

    private final String name;

    PayStatus(String name) {
        this.name = name;
    }

    public static PayStatus fromName(String type) {
        return PayStatus.valueOf(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
