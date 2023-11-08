package ru.garrowd.scheduleattendanceservice.services.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import ru.garrowd.scheduleattendanceservice.models.AcademicSubject;
import ru.garrowd.scheduleattendanceservice.services.AcademicSubjectsService;


/*@GRpcService
@Slf4j
@RequiredArgsConstructor
public class SubjectsServiceImpl
        extends AcademicServiceGrpc.AcademicServiceImplBase {
    private final AcademicSubjectsService academicSubjectsService;

    @Override
    public void createAcademicSubject(
            AcademicSubjectRequest request,
            StreamObserver<AcademicSubjectResponse> responseObserver) {
        AcademicSubject academicSubject = academicSubjectsService.create(AcademicSubject.builder()
                                                                                        .title(request.getTitle())
                                                                                        .direction(request.getDirection())
                                                                                        .teacherFullName(request.getTeacherFullName())
                                                                                        .teacherId(request.getTeacherId())
                                                                                        .build());

        AcademicSubjectResponse response =
                AcademicSubjectResponse.newBuilder().setAcademicSubjectId(academicSubject.getId()).build();


        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}*/

