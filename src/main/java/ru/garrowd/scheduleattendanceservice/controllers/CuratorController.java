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
import ru.garrowd.scheduleattendanceservice.services.AcademicSubjectsService;
import ru.garrowd.scheduleattendanceservice.services.validators.ValidationService;
import ru.garrowd.scheduleattendanceservice.models.Lesson;
import ru.garrowd.scheduleattendanceservice.models.extras.LessonsDay;
import ru.garrowd.scheduleattendanceservice.services.LessonsService;
import ru.garrowd.scheduleattendanceservice.utils.Token;
import ru.garrowd.scheduleattendanceservice.utils.enums.JwtClaims;

import java.util.Map;

@PreAuthorize("hasRole('CURATOR')")
@RestController
@RequestMapping("/curator")
@RequiredArgsConstructor
@Slf4j
public class CuratorController {
    private final LessonsService lessonsService;
    private final AcademicSubjectsService academicSubjectsService;
    private final ModelMapper modelMapper;

    private final ValidationService validationService;

    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    public Map<Integer, LessonsDay> getScheduleCurator(@RequestParam(name = "group") String group) {
        return lessonsService.getLessonsDayByGroup(group);
    }

    @GetMapping("/academic-subjects")
    @ResponseStatus(HttpStatus.OK)
    public Page<AcademicSubject> getAllSubjects(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size, JwtAuthenticationToken token) {
        return academicSubjectsService.getAllAcademicSubjectsByDirection(Token.get(token, JwtClaims.DIRECTION), PageRequest.of(page, size));
    }

    @GetMapping("/schedule/create")
    @ResponseStatus(HttpStatus.OK)
    public LessonPayload.Request createLesson(
            @RequestBody LessonPayload.Create lessonPayload, JwtAuthenticationToken token) {
        validationService.validate(lessonPayload);

        return modelMapper.map(lessonsService.create(modelMapper.map(lessonPayload, Lesson.class), lessonPayload.getSubjectId(), Token.get(token, JwtClaims.DIRECTION)),
                               LessonPayload.Request.class);
    }

    @DeleteMapping("/schedule/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLesson(@PathVariable(name = "id") String id, JwtAuthenticationToken token) {
        lessonsService.delete(id, Token.get(token, JwtClaims.DIRECTION));
    }
}
