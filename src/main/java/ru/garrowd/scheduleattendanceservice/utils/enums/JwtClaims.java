package ru.garrowd.scheduleattendanceservice.utils.enums;

import lombok.Getter;

@Getter

public enum JwtClaims {
    SUB("sub"),
    FULL_NAME("full_name"),
    PICTURE("img_url"),
    EMAIL("email"),
    PHONE_NUMBER("phone_number"),
    GROUP("group"),
    DIRECTION("direction"),
    AUTHORITY("authority"),
    DEPARTMENT("department"),
    ROLES("authority");

    private final String value;

    JwtClaims(String value) {
        this.value = value;
    }
}
