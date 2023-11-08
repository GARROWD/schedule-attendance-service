package ru.garrowd.scheduleattendanceservice.models.projections;

import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;

import java.time.LocalTime;

public interface LessonAttendance {
    AcademicSubject getAcademicSubject();

    LocalTime getStartTime();

    LocalTime getEndTime();
}
