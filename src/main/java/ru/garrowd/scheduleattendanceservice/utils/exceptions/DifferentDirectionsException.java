package ru.garrowd.scheduleattendanceservice.utils.exceptions;

import ru.garrowd.scheduleattendanceservice.utils.exceptions.generics.GenericException;

public class DifferentDirectionsException
        extends GenericException {
    public DifferentDirectionsException(String message){
        super(message);
    }
}
