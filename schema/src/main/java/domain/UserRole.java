package domain;

public enum UserRole {
    CARRIER("carrier"), USER("user");

    private final String role;

    UserRole(String role){ this.role = role;}

    public String getRole() {
        return role;
    }
}
