package ru.garrowd.scheduleattendanceservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;
import ru.garrowd.scheduleattendanceservice.utils.enums.LessonTypes;

import java.time.LocalTime;


public class AttendancePayload {
    @Schema(name = "AttendancePayloadCreate")
    @Data
    public static class Create {
        @Size(max = 40, message = "id.error.maxSize")
        @NotNull(message = "attendance.error.nullFields")
        private String studentId;

        @NotNull(message = "attendance.error.nullFields")
        private Boolean attended;
    }

    @Data
    public static class Update {

    }

    @Data
    public static class Request {

    }
}
