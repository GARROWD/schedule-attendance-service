package ru.garrowd.scheduleattendanceservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.garrowd.scheduleattendanceservice.models.Attendance;
import ru.garrowd.scheduleattendanceservice.models.extras.AcademicSubjectAttendance;
import ru.garrowd.scheduleattendanceservice.models.extras.LessonsDay;
import ru.garrowd.scheduleattendanceservice.services.AcademicSubjectsService;
import ru.garrowd.scheduleattendanceservice.services.AttendanceService;
import ru.garrowd.scheduleattendanceservice.services.LessonsService;
import ru.garrowd.scheduleattendanceservice.utils.Token;
import ru.garrowd.scheduleattendanceservice.utils.enums.JwtClaims;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final LessonsService lessonsService;
    private final AttendanceService attendanceService;
    private final AcademicSubjectsService academicSubjectsService;

    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    public Map<Integer, LessonsDay> getSchedule(
            @RequestParam(name = "group", defaultValue = "null") String group, JwtAuthenticationToken token) {
        // TODO Ну и костыль
        return lessonsService.getLessonsDayByGroup("null".equals(group) ? Token.get(token, JwtClaims.GROUP) : group);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/academic-subjects-attendance")
    @ResponseStatus(HttpStatus.OK)
    public Page<AcademicSubjectAttendance> getAllSubjects(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size, JwtAuthenticationToken token) {
        return academicSubjectsService.getAllAcademicSubjectsAttendanceByStudentIdAndDirection(Token.get(token, JwtClaims.SUB), Token.get(token, JwtClaims.DIRECTION), PageRequest.of(page, size));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/attendance")
    public Page<Attendance> findAllByAcademicSubjectIdAndStudentId(
            @RequestParam(name = "academicSubjectId") String academicSubjectId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size, JwtAuthenticationToken token) {
        return attendanceService.findAllByAcademicSubjectIdAndStudentId(academicSubjectId,
                                                                           Token.get(token, JwtClaims.SUB),
                                                                           PageRequest.of(page, size));
    }
}