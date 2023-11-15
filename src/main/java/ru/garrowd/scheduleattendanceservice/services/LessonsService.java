package ru.garrowd.scheduleattendanceservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;
import ru.garrowd.scheduleattendanceservice.models.Lesson;
import ru.garrowd.scheduleattendanceservice.models.extras.LessonsDay;
import ru.garrowd.scheduleattendanceservice.repositories.LessonsRepository;
import ru.garrowd.scheduleattendanceservice.services.validators.ValidationService;
import ru.garrowd.scheduleattendanceservice.utils.enums.ExceptionMessages;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.DifferentDirectionsException;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.LessonCollisionException;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.NotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LessonsService {
    private final ExceptionMessagesService exceptionMessages;
    private final LessonsRepository lessonsRepository;

    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    private final AcademicSubjectsService academicSubjectsService;

    public Lesson getById(String id)
            throws NotFoundException {
        Optional<Lesson> foundLesson = lessonsRepository.findById(id);

        return foundLesson.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.LESSON_NOT_FOUND, id.toString())));
    }

    public void existsById(String id)
            throws NotFoundException {
        lessonsRepository.findById(id).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.LESSON_NOT_FOUND, id.toString())));
    }

    public Lesson getCurrentByTeacherId(String teacherId){
        LocalDateTime current = LocalDateTime.now();

        return lessonsRepository.findByWeekDayAndAcademicSubject_TeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThan(current.getDayOfWeek().getValue(), teacherId, current.toLocalTime(), current.toLocalTime()).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.LESSON_NOT_FOUND)));
    }

    public Lesson getByTeacherIdAndDateAndStartTimeAndEndTime(String teacherId, LocalDate date,
                                                              LocalTime startTime, LocalTime endTime){
        return lessonsRepository.findByWeekDayAndAcademicSubject_TeacherIdAndStartTimeLessThanEqualAndEndTimeGreaterThan(date.getDayOfWeek().getValue(), teacherId, startTime, endTime).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.LESSON_NOT_FOUND)));
    }

    public Map<Integer, LessonsDay> groupLessonsByWeekDay(List<Lesson> lessons) {
        Map<Integer, LessonsDay> LessonsDays = new HashMap<>();

        lessons.stream().collect(Collectors.groupingBy(Lesson::getWeekDay)).forEach((weekDay, dayLessons) -> {
            LocalDate date = LocalDate.now().with(DayOfWeek.of(weekDay));
            int numberOfLessons = dayLessons.size();

            LocalTime firstLessonTime = dayLessons.stream().map(Lesson::getStartTime).min(LocalTime::compareTo).orElse(
                    null);
            LocalTime lastLessonTime = dayLessons.stream().map(Lesson::getEndTime).max(LocalTime::compareTo).orElse(
                    null);

            LessonsDays.put(weekDay,
                            new LessonsDay(date, numberOfLessons, firstLessonTime, lastLessonTime, dayLessons));
        });

        return LessonsDays;
    }

    // Блин, а если несколько серверов, то как проверять, что кеш актуален...
    //@Cacheable(value = "LessonsDayByGroup", key = "#group")
    public Map<Integer, LessonsDay> getLessonsDayByGroup(String group) {
        return groupLessonsByWeekDay(lessonsRepository.findAllByGroup(group));
    }

    public Map<Integer, LessonsDay> getLessonsDayTeacherId(String teacherId) {
        return groupLessonsByWeekDay(lessonsRepository.findAllByAcademicSubject_TeacherId(teacherId));
    }

    public boolean hasCollision(List<Lesson> Lessons, Lesson Lesson) {
        return Lessons.stream().anyMatch(otherLesson -> Lesson.getStartTime().isBefore(otherLesson.getEndTime())
                                                        && Lesson.getEndTime().isAfter(otherLesson.getStartTime()));
    }

    //@CacheEvict(value = "LessonsDayByGroup", key = "#Lesson.group")
    @Transactional
    public Lesson create(Lesson lesson, String subjectId, String direction) {
        if(lesson.getEndTime().isBefore(lesson.getStartTime())){
            throw new LessonCollisionException(exceptionMessages.getMessage(ExceptionMessages.LESSON_INVALID_TIME_RANGE));
        }

        AcademicSubject academicSubject = academicSubjectsService.getById(subjectId);

        if(!academicSubject.getDirection().equals(direction)){
            throw new DifferentDirectionsException(exceptionMessages.getMessage(ExceptionMessages.LESSON_INVALID_DIRECTION));
        }

        lesson.setAcademicSubject(academicSubject);

        // TODO Желательно сделать gRpc запрос для того, чтобы проверить, есть ли такая группа

        // TODO Так-то предметы в списке могут повторятся. А ведь можно было и @Query("SELECT l FROM Lesson l WHERE l
        //  .weekDay = :weekDay AND (l.group = :group OR l.teacherId = :teacherId)")
        List<Lesson> lessons = Stream.concat(
                lessonsRepository.findAllByWeekDayAndGroup(lesson.getWeekDay(), lesson.getGroup()).stream(),
                lessonsRepository.findAllByWeekDayAndAcademicSubject_TeacherId(lesson.getWeekDay(),
                                                                               lesson.getAcademicSubject()
                                                                                     .getTeacherId()).stream()).toList();

        if(hasCollision(lessons, lesson)) {
            throw new LessonCollisionException(exceptionMessages.getMessage(ExceptionMessages.LESSON_COLLISION));
        }

        lessonsRepository.save(lesson);

        log.info("Lesson with ID {} is created", lesson.getId());

        return lesson;
    }

    // TODO Как правильно делается редактирование?
    @Transactional
    public Lesson update(Lesson unsavedLesson, String id) {
        Lesson lesson = ru.garrowd.scheduleattendanceservice.models.Lesson.builder().build();
        modelMapper.map(getById(id), lesson);
        modelMapper.map(unsavedLesson, lesson);
        validationService.validate(lesson);
        lessonsRepository.save(lesson);

        log.info("Lesson with ID {} is updated", lesson.getId());

        return lesson;
    }

    @Transactional
    public void updateWithoutChecks(Lesson lesson) {
        existsById(lesson.getId());
        lessonsRepository.save(lesson);

        log.info("Lesson with ID {} is updated", lesson.getId());
    }

    //@CacheEvict(value = "LessonsDayByGroup", key = "LessonsService.getById(id).group")
    @Transactional
    public void delete(String id, String direction)
            throws NotFoundException {
        // TODO Надо ли после той магии сверху проверять здесь exists?
        if(getById(id).getAcademicSubject().getDirection().equals(direction)){
            throw new DifferentDirectionsException(exceptionMessages.getMessage(ExceptionMessages.LESSON_INVALID_DIRECTION));
        }
        lessonsRepository.deleteById(id);

        log.info("Lesson with ID {} is deleted", id);
    }
}
