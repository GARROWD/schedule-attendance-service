package ru.garrowd.scheduleattendanceservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;
import ru.garrowd.scheduleattendanceservice.utils.enums.LessonTypes;

import java.time.LocalTime;

public class LessonPayload {
    @Schema(name = "LessonPayloadCreate")
    @Data
    public static class Create {
        @NotNull(message = "lesson.error.nullField")
        private String subjectId;

        @NotNull(message = "lesson.error.nullField")
        private Integer weekDay;

        @NotNull(message = "lesson.error.nullField")
        private LocalTime startTime;

        @NotNull(message = "lesson.error.nullField")
        private LocalTime endTime;

        @NotNull(message = "lesson.error.nullField")
        @Enumerated(EnumType.STRING)
        private LessonTypes type;

        @NotNull(message = "lesson.error.nullField")
        private String group;

        private String room;
    }

    @Data
    public static class Update {

    }

    @Schema(name = "LessonPayloadRequest")
    @Data
    public static class Request {
        private AcademicSubject academicSubject;

        private Integer weekDay;

        private LocalTime startTime;

        private LocalTime endTime;

        @Enumerated(EnumType.STRING)
        private LessonTypes type;

        private String group;

        private String room;
    }
}

