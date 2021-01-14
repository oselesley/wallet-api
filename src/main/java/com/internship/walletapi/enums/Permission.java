package com.internship.walletapi.enums;

public enum Permission {
    NOOB_READ("noob:read"),
    NOOB_WRITE("noob:write"),
    ELITE_READ("elite:read"),
    ELITE_WRITE("elite:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
