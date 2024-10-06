package server.domain.pay.model;

public enum PayMethod {
    CREDIT("CREDIT"),
    ACCOUNT("ACCOUNT");

    private final String name;

    PayMethod(String name) {
        this.name = name;
    }

    public static PayMethod fromName(String type) {
        return PayMethod.valueOf(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
