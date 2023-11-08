package ru.garrowd.scheduleattendanceservice.models.extras;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.garrowd.scheduleattendanceservice.models.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonsDay {
    private LocalDate date;

    private Integer numberOfLessons;

    private LocalTime firstLessonTime;

    private LocalTime  lastLessonTime;

    private List<Lesson> lessons;
}