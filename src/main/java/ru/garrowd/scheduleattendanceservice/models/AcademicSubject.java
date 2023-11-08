package ru.garrowd.scheduleattendanceservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "academic_subjects")
public class AcademicSubject {
    @Id
    @Column(name = "id", unique = true, length = 40)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "direction", nullable = false, length = 200)
    private String direction;

    @Column(name = "teacher_full_name", nullable = false, length = 200)
    private String teacherFullName;

    @Column(name = "teacher_id", nullable = false, length = 40)
    private String teacherId;
}
