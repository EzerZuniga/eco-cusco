package com.cusco.limpio.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ReportStatus {
    PENDIENTE("PENDIENTE", "Pendiente"),
    EN_PROCESO("EN_PROCESO", "En proceso"),
    RESUELTO("RESUELTO", "Resuelto");

    private final String code;
    private final String displayName;

    ReportStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return code;
    }

    @JsonCreator
    public static ReportStatus from(String value) {
        if (value == null) return null;
        String normalized = value.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(s -> s.code.equalsIgnoreCase(normalized) || s.name().equalsIgnoreCase(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ReportStatus: " + value));
    }
}