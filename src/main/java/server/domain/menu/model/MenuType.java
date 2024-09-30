package server.domain.menu.model;

public enum MenuType {
    MAIN, SIDE, DRINK;

    public static MenuType fromString(String type) {
        switch (type) {
            case "MAIN":
                return MAIN;
            case "SIDE":
                return SIDE;
            case "DRINK":
                return DRINK;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
