package ru.garrowd.scheduleattendanceservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.garrowd.scheduleattendanceservice.utils.enums.LessonTypes;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @Column(name = "id", unique = true, length = 40)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private AcademicSubject academicSubject;

    @Column(name = "week_day", nullable = false)
    private Integer weekDay;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 200)
    private LessonTypes type;

    @Column(name = "group_name", nullable = false, length = 200)
    private String group;

    @Column(name = "room", length = 200)
    private String room;
}
