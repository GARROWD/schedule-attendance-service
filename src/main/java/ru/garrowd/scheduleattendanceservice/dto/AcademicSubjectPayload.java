package ru.garrowd.scheduleattendanceservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


public class AcademicSubjectPayload {
    @Schema(name = "AcademicSubjectPayloadCreate")
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

    @Schema(name = "AcademicSubjectPayloadRequest")
    @Data
    public static class Request {

    }
}
