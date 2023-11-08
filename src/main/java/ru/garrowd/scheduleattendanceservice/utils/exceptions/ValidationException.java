package ru.garrowd.scheduleattendanceservice.utils.exceptions;

import ru.garrowd.scheduleattendanceservice.utils.exceptions.generics.GenericExceptionWithDetails;
import ru.garrowd.scheduleattendanceservice.utils.exceptions.messages.GenericMessage;
import java.util.Set;

public class ValidationException
        extends GenericExceptionWithDetails {
    public ValidationException(Set<GenericMessage> messages) {
        super(messages);
    }
}
