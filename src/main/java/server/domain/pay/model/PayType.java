package server.domain.pay.model;

public enum PayType {
    TOGETHER("TOGETHER"),
    ALONE("ALONE");

    private final String name;

    PayType(String name) {
        this.name = name;
    }

    public static PayType fromName(String type) {
        return PayType.valueOf(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
