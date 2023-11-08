package ru.garrowd.scheduleattendanceservice.utils.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    LESSON_NOT_FOUND("lesson.error.notFound"),
    LESSON_COLLISION("lesson.error.collision"),
    LESSON_INVALID_TIME_RANGE("lesson.error.invalidTimeRange"),
    LESSON_INVALID_DIRECTION("lesson.error.invalidDirection"),

    ACADEMIC_SUBJECT_NOT_FOUND("academicSubject.error.notFound"),
    ATTENDANCE_NOT_FOUND("attendance.error.notFound"),
    ATTENDANCE_CREATE_LIST_INVALID("attendance.error.createListInvalid"),

    REQUEST_METHOD_NOT_SUPPORT("request.error.notSupport"),
    REQUEST_PARAMETER_CONVERT_FAILED("request.error.parameterConvertFailed"),
    REQUEST_MISSING_BODY("request.error.missingBody"),
    REQUEST_MISSING_PARAMETER("request.error.missingParameter"),
    REQUEST_ARGUMENT_NOT_VALID("request.error.requestArgumentNotValid");

    private final String value;

    ExceptionMessages(String value) {
        this.value = value;
    }
}