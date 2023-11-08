package ru.garrowd.scheduleattendanceservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.garrowd.scheduleattendanceservice.dto.LessonPayload;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;
import ru.garrowd.scheduleattendanceservice.models.Attendance;
import ru.garrowd.scheduleattendanceservice.models.extras.LessonsDay;
import ru.garrowd.scheduleattendanceservice.models.projections.LessonAttendance;
import ru.garrowd.scheduleattendanceservice.services.AcademicSubjectsService;
import ru.garrowd.scheduleattendanceservice.services.AttendanceService;
import ru.garrowd.scheduleattendanceservice.services.LessonsService;
import ru.garrowd.scheduleattendanceservice.utils.Token;
import ru.garrowd.scheduleattendanceservice.utils.enums.JwtClaims;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@PreAuthorize("hasRole('TEACHER')")
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {
    private final ModelMapper modelMapper;
    private final LessonsService lessonsService;
    private final AttendanceService attendanceService;
    private final AcademicSubjectsService academicSubjectsService;

    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    public Map<Integer, LessonsDay> getScheduleForTeacher(JwtAuthenticationToken token) {
        return lessonsService.getLessonsDayTeacherId(Token.get(token, JwtClaims.SUB));
    }

    @GetMapping("/schedule/current")
    public LessonPayload.Request getCurrentLesson(JwtAuthenticationToken token) {
        return modelMapper.map(lessonsService.getCurrentByTeacherId(Token.get(token, JwtClaims.SUB)),
                               LessonPayload.Request.class);
    }

    @GetMapping("/academic-subjects")
    @ResponseStatus(HttpStatus.OK)
    public Page<AcademicSubject> getAllSubjects(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size, JwtAuthenticationToken token) {
        return academicSubjectsService.getAllAcademicSubjectsByTeacherId(Token.get(token, JwtClaims.SUB),
                                                              PageRequest.of(page, size));
    }

    @GetMapping("/lessons")
    @ResponseStatus(HttpStatus.OK)
    public Page<LessonAttendance> getAllLessonAttendancesByDate(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size, JwtAuthenticationToken token) {
        return attendanceService.getAllLessonAttendancesByDate(date, Token.get(token, JwtClaims.SUB), PageRequest.of(page, size));
    }

    // TODO Простите меня за этот код, я пишу его, чтобы он просто работал...

    @GetMapping("/attendance/{academicSubjectId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Attendance> getAllAttendanceByAcademicSubjectIdAndDate(
            @PathVariable(name = "academicSubjectId") String academicSubjectId,
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "startTime") LocalTime startTime,
            @RequestParam(name = "endTime") LocalTime endTime,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size, JwtAuthenticationToken token) {
        return attendanceService.findAllByAcademicSubjectIdAndDateAndStartTimeAndEndTime(academicSubjectId,
                                                                           Token.get(token, JwtClaims.SUB), date,
                                                                           startTime, endTime,
                                                                           PageRequest.of(page, size));
    }

    // TODO Простите меня за этот код, я пишу его, чтобы он просто работал...

    @GetMapping("/attendance/{academicSubjectId}/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Attendance> findAllByAcademicSubjectIdAndStudentId(
            @PathVariable(name = "academicSubjectId") String academicSubjectId,
            @PathVariable(name = "studentId") String studentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return attendanceService.findAllByAcademicSubjectIdAndStudentId(academicSubjectId, studentId, PageRequest.of(page, size));
    }

    // TODO Простите меня за этот код, я пишу его, чтобы он просто работал...

    @PostMapping("/attendance")
    @ResponseStatus(HttpStatus.OK)
    public void attendanceCreateByStudentId(
            @RequestParam(name = "attendanceId") String attendanceId, JwtAuthenticationToken token) {
        attendanceService.switchAttendanceByAcademicSubjectIdAndStudentId(attendanceId,
                                                                          Token.get(token, JwtClaims.SUB));
    }
}
