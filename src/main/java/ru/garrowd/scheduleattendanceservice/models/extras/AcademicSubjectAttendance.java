package ru.garrowd.scheduleattendanceservice.models.extras;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSubjectAttendance {
    private Integer numberOfAttended;

    private Integer numberOfLessons;

    private AcademicSubject academicSubject;
}
