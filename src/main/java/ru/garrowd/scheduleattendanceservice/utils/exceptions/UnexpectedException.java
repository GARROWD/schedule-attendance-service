package ru.garrowd.scheduleattendanceservice.utils.exceptions;

import ru.garrowd.scheduleattendanceservice.utils.exceptions.generics.GenericException;

public class UnexpectedException extends GenericException {
    public UnexpectedException(String message){
        super(message);
    }
}
