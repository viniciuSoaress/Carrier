package domain;

public enum UserRole {
    BOSS("boss"), ADMIN("admin"), USER("user");

    private String role;

    UserRole(String role){ this.role = role;}

    public String getRole() {
        return role;
    }
}
