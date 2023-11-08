package ru.garrowd.scheduleattendanceservice.services;

import com.university.userservice.grpc.user.GroupServiceGrpc;
import com.university.userservice.grpc.user.StudentsByGroupAndDirectionResponse;
import com.university.userservice.grpc.user.StudentsByGroupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.scheduleattendanceservice.models.Attendance;
import ru.garrowd.scheduleattendanceservice.models.Lesson;
import ru.garrowd.scheduleattendanceservice.models.projections.LessonAttendance;
import ru.garrowd.scheduleattendanceservice.repositories.AttendanceRepository;
import ru.garrowd.scheduleattendanceservice.services.validators.ValidationService;
import ru.garrowd.scheduleattendanceservice.utils.enums.ExceptionMessages;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttendanceService {
    private final ExceptionMessagesService exceptionMessages;
    private final AttendanceRepository attendanceRepository;

    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    private final LessonsService lessonsService;

    private final GroupServiceGrpc.GroupServiceBlockingStub stub;

    public Attendance getById(String id)
            throws NotFoundException {
        Optional<Attendance> foundAttendance = attendanceRepository.findById(id);

        return foundAttendance.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ATTENDANCE_NOT_FOUND, id.toString())));
    }

    public void existsById(String id)
            throws NotFoundException {
        attendanceRepository.findById(id).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ATTENDANCE_NOT_FOUND, id.toString())));
    }

    public Page<LessonAttendance> getAllLessonAttendancesByDate(LocalDate date, String teacherId, Pageable pageable) {
        return attendanceRepository.findDistinctAcademicSubjectByDateAndAcademicSubject_TeacherIdOrderByStartTime(date,
                                                                                                                  teacherId,
                                                                                                                  pageable);
    }

    public Page<Attendance> findAllByAcademicSubjectIdAndStudentId(
            String academicSubjectId, String studentId, Pageable pageable) {
        return attendanceRepository.findAllByAcademicSubject_IdAndStudentIdOrderByDate(academicSubjectId,
                                                                                       studentId, pageable);
    }

    public Page<Attendance> findAllByAcademicSubjectIdAndDateAndStartTimeAndEndTime(
            String academicSubjectId, String teacherId, LocalDate date,
            LocalTime startTime, LocalTime endTime, Pageable pageable) {
        // TODO По хорошему я должен здесь (и не только) проверить, принадлежит ли предмет учителю, и выбрасывать
        //  ошибку если нет. А то эта обязанность перекладывается на бд, а он просто возвращает пустой page, если
        //  предмет не его. Ведь так?
        //  И вообще, этот метод мне не нравится, все выглядит костыльным и не гибким. Информация о посещении
        //  студентов будет создана только если преподаватель нажмет на кнопку получения посещаемости этой конкретной
        //  пары. Да уж, можно было конечно завязать это на @Scheduled и на информацию о расписание, но расписание
        //  само по себе такое шаткое и будто ни на что не валяющее, что страшно.
        //  Короче я точно знаю, что то что здесь написано - полная лажа
        Page<Attendance> attendancePage =
                attendanceRepository.findAllByAcademicSubject_IdAndAcademicSubject_TeacherIdAndStartTimeAndEndTimeAndDateOrderById(
                        academicSubjectId, teacherId, startTime, endTime, date, pageable);

        if(! attendancePage.getContent().isEmpty()) {
            return attendancePage;
        }

        Lesson lesson = new Lesson(); // lessonsService.getByTeacherIdAndDateAndStartTimeAndEndTime(teacherId, date, startTime, endTime);

        StudentsByGroupAndDirectionResponse students = stub.getStudentsByGroupAndDirection(
                StudentsByGroupRequest.newBuilder()
                                      .setDirectionTitle(lesson.getAcademicSubject().getDirection())
                                      .setGroupTitle(lesson.getGroup()).build());

        List<Attendance> attendanceList = new ArrayList<>();

        if(students != null) {
            LocalDate localDate = LocalDate.now();

            attendanceList = students.getStudentsList().stream().map(
                    student -> Attendance
                            .builder()
                            .academicSubject(lesson.getAcademicSubject())
                            .studentId(student.getId())
                            .date(localDate)
                            .startTime(startTime)
                            .endTime(endTime)
                            .attended(false)
                            .build()).toList();

            attendanceRepository.saveAll(attendanceList);
        }

        return new PageImpl<>(attendanceList, pageable, attendanceList.size());
    }

    @Transactional
    public void switchAttendanceByAcademicSubjectIdAndStudentId(String attendanceId, String teacherId) {
        // TODO Опять же, бд выполняет проверку, есть ли такая посещаемость, у которого будет предмет, который ведет
        //  текущий преподаватель. Есть ощущение, будто это плохо
        Optional<Attendance> foundAttendance = attendanceRepository.findByIdAndAcademicSubject_TeacherId(attendanceId,
                                                                                                         teacherId);

        Attendance attendance = foundAttendance.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ATTENDANCE_NOT_FOUND, attendanceId)));

        attendance.setAttended(! attendance.getAttended());

        attendanceRepository.save(attendance);
    }

    public Integer numberOfAttended(String studentId, String academicSubjectId) {
        return attendanceRepository.countAllByStudentIdAndAcademicSubject_Id(studentId, academicSubjectId);
    }

    public Integer numberOfLessons(String academicSubjectId) {
        return attendanceRepository.countAllByAcademicSubject_Id(academicSubjectId);
    }

    // TODO Как правильно делается редактирование?
    @Transactional
    public Attendance update(Attendance unsavedAttendance, String id) {
        Attendance attendance = Attendance.builder().build();
        modelMapper.map(getById(id), attendance);
        modelMapper.map(unsavedAttendance, attendance);
        validationService.validate(attendance);
        attendanceRepository.save(attendance);

        log.info("Attendance with ID {} is updated", attendance.getId());

        return attendance;
    }

    @Transactional
    public void updateWithoutChecks(Attendance attendance) {
        existsById(attendance.getId());
        attendanceRepository.save(attendance);

        log.info("Attendance with ID {} is updated", attendance.getId());
    }

    @Transactional
    public void delete(String id)
            throws NotFoundException {
        existsById(id);
        attendanceRepository.deleteById(id);

        log.info("Attendance with ID {} is deleted", id);
    }
}
