package ru.garrowd.scheduleattendanceservice.utils.exceptions;

import ru.garrowd.scheduleattendanceservice.utils.exceptions.generics.GenericException;

public class NotFoundException
        extends GenericException {
    public NotFoundException(String message){
        super(message);
    }
}