package ru.garrowd.scheduleattendanceservice.utils.exceptions.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.garrowd.scheduleattendanceservice.utils.enums.ExceptionMessages;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.DifferentDirectionsException;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.LessonCollisionException;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.NotFoundException;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.ValidationException;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.messages.GenericMessage;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.messages.GenericMessageTimestamp;
import ru.garrowd.scheduleattendanceservice.services.ExceptionMessagesService;

import java.util.Set;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionsHandler {
    private final ExceptionMessagesService exceptionMessages;

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericMessageTimestamp runtimeException(
            RuntimeException exception) {
        return new GenericMessageTimestamp(exception.getMessage(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public GenericMessage httpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return new GenericMessage(exceptionMessages.getMessage(ExceptionMessages.REQUEST_METHOD_NOT_SUPPORT),
                                  exception.getMethod());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMessage methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        return new GenericMessage(exceptionMessages.getMessage(ExceptionMessages.REQUEST_PARAMETER_CONVERT_FAILED),
                                  exception.getName());

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMessage httpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        return new GenericMessage(exceptionMessages.getMessage(ExceptionMessages.REQUEST_MISSING_BODY), null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMessage missingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        return new GenericMessage(exceptionMessages.getMessage(ExceptionMessages.REQUEST_MISSING_PARAMETER),
                                  exception.getParameterName());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMessage methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new GenericMessage(exceptionMessages.getMessage(ExceptionMessages.REQUEST_ARGUMENT_NOT_VALID),
                                  exception.getParameter().getParameterName());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Set<GenericMessage> validationException(ValidationException exception) {
        return exception.getGenericMessageWithDetails();
    }

    // TODO Как-то странно ошибки от GenericException сделаны, почему сам exception не содержит класс GenericMessageTimestamp

    @ExceptionHandler(LessonCollisionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMessageTimestamp lessonCollisionException(LessonCollisionException exception) {
        return new GenericMessageTimestamp(exception.getMessage());
    }

    @ExceptionHandler(DifferentDirectionsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericMessageTimestamp attendanceListInvalidException(DifferentDirectionsException exception) {
        return new GenericMessageTimestamp(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericMessageTimestamp customerNotFoundException(NotFoundException exception) {
        return new GenericMessageTimestamp(exception.getMessage());
    }
}

