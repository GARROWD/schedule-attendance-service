package ru.garrowd.scheduleattendanceservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;

@Repository
public interface AcademicSubjectsRepository
        extends JpaRepository<AcademicSubject, String> {
    Page<AcademicSubject> findAllByTeacherIdOrderByTitle(String teacherId, Pageable pageable);
    Page<AcademicSubject> findAllByDirectionOrderByTitle(String direction, Pageable pageable);

}
