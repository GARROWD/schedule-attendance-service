package ru.garrowd.scheduleattendanceservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.scheduleattendanceservice.models.Lesson;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonsRepository
        extends JpaRepository<Lesson, String> {
    List<Lesson> findAllByGroup(String group);
    List<Lesson> findAllByAcademicSubject_TeacherId(String teacherId);
    List<Lesson> findAllByWeekDayAndAcademicSubject_TeacherId(Integer weekDay, String teacherID);
    List<Lesson> findAllByWeekDayAndGroup(Integer weekDay, String group);

    Optional<Lesson> findByWeekDayAndAcademicSubject_TeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThan(
            Integer weekDay, String teacherId, LocalTime startTime, LocalTime endTime);
}
