package ru.garrowd.scheduleattendanceservice.utils.enums;

public enum LessonTypes {
    FULL_TIME("FULL_TIME"),
    REMOTE("REMOTE");

    private final String value;

    LessonTypes(String value) {
        this.value = value;
    }
}
