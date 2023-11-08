package ru.garrowd.scheduleattendanceservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.scheduleattendanceservice.models.Attendance;
import ru.garrowd.scheduleattendanceservice.models.projections.LessonAttendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface AttendanceRepository
        extends JpaRepository<Attendance, String> {
    Integer countAllByAcademicSubject_Id(String academicSubjectId);

    Integer countAllByStudentIdAndAcademicSubject_Id(String studentId, String academicSubjectId);

    Page<LessonAttendance> findDistinctAcademicSubjectByDateAndAcademicSubject_TeacherIdOrderByStartTime(
            LocalDate date, String teacherId, Pageable pageable);

    Page<Attendance> findAllByAcademicSubject_IdAndStudentIdAndAcademicSubject_TeacherIdOrderByDate(String academicSubjectId, String teacherId, String studentId, Pageable pageable);

    Page<Attendance> findAllByAcademicSubject_IdAndStudentIdOrderByDate(String academicSubjectId, String studentId, Pageable pageable);

    Page<Attendance> findAllByAcademicSubject_IdAndAcademicSubject_TeacherIdAndStartTimeAndEndTimeAndDateOrderById(String academicSubjectId, String teacherId, LocalTime startTime, LocalTime endTime, LocalDate date, Pageable pageable);

    Optional<Attendance> findByIdAndAcademicSubject_TeacherId(String id, String teacherId);

}
