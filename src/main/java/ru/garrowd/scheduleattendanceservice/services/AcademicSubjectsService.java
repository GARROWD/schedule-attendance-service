package ru.garrowd.scheduleattendanceservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;
import ru.garrowd.scheduleattendanceservice.models.extras.AcademicSubjectAttendance;
import ru.garrowd.scheduleattendanceservice.repositories.AcademicSubjectsRepository;
import ru.garrowd.scheduleattendanceservice.services.validators.ValidationService;
import ru.garrowd.scheduleattendanceservice.utils.enums.ExceptionMessages;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AcademicSubjectsService {
    private final AttendanceService attendanceService;

    private final ExceptionMessagesService exceptionMessages;
    private final AcademicSubjectsRepository academicSubjectsRepository;

    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    public AcademicSubject getById(String id)
            throws NotFoundException {
        Optional<AcademicSubject> foundSubject = academicSubjectsRepository.findById(id);

        return foundSubject.orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ACADEMIC_SUBJECT_NOT_FOUND, id.toString())));
    }

    public void existsById(String id)
            throws NotFoundException {
        academicSubjectsRepository.findById(id).orElseThrow(() -> new NotFoundException(
                exceptionMessages.getMessage(ExceptionMessages.ACADEMIC_SUBJECT_NOT_FOUND, id.toString())));
    }

    public Page<AcademicSubjectAttendance> getAllAcademicSubjectsAttendanceByStudentIdAndDirection(
            String studentId, String direction, Pageable pageable) {
        Page<AcademicSubject> academicSubjectPage = academicSubjectsRepository.findAllByDirectionOrderByTitle(direction,
                                                                                                              pageable);

        List<AcademicSubjectAttendance> academicSubjectAttendanceList = academicSubjectPage.getContent().stream().map(
                academicSubject ->
                        AcademicSubjectAttendance.builder().numberOfAttended(attendanceService.numberOfAttended(
                                                                 studentId, academicSubject.getId()))
                                                 .numberOfLessons(attendanceService.numberOfLessons(academicSubject.getId()))
                                                 .academicSubject(academicSubject)
                                                 .build()).toList();

        return new PageImpl<>(academicSubjectAttendanceList, pageable, academicSubjectPage.getTotalElements());
    }

    public Page<AcademicSubject> getAllAcademicSubjectsByDirection(String direction, Pageable pageable) {
        return academicSubjectsRepository.findAllByDirectionOrderByTitle(direction, pageable);
    }

    public Page<AcademicSubject> getAllAcademicSubjectsByTeacherId(String teacherId, Pageable pageable) {
        return academicSubjectsRepository.findAllByTeacherIdOrderByTitle(teacherId, pageable);
    }

    @Transactional
    public AcademicSubject create(AcademicSubject academicSubject) {

        academicSubjectsRepository.save(academicSubject);

        log.info("Subject with ID {} is created", academicSubject.getId());

        return academicSubject;
    }

    // TODO Как правильно делается редактирование?
    @Transactional
    public AcademicSubject update(AcademicSubject unsavedAcademicSubject, String id) {
        AcademicSubject academicSubject = AcademicSubject.builder().build();
        modelMapper.map(getById(id), academicSubject);
        modelMapper.map(unsavedAcademicSubject, academicSubject);
        validationService.validate(academicSubject);
        academicSubjectsRepository.save(academicSubject);

        log.info("Subject with ID {} is updated", academicSubject.getId());

        return academicSubject;
    }

    @Transactional
    public void updateWithoutChecks(AcademicSubject academicSubject) {
        existsById(academicSubject.getId());
        academicSubjectsRepository.save(academicSubject);

        log.info("Subject with ID {} is updated", academicSubject.getId());
    }
}
