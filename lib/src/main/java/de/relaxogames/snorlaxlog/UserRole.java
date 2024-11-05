package de.relaxogames.snorlaxlog;

public enum UserRole {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user"),
    GUEST("guest");

    private final String apiValue;

    UserRole(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }
}