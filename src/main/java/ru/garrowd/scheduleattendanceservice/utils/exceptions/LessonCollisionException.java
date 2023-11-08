package ru.garrowd.scheduleattendanceservice.utils.exceptions;

import ru.garrowd.scheduleattendanceservice.utils.exceptions.generics.GenericException;

public class LessonCollisionException extends GenericException {
    public LessonCollisionException(String message){
        super(message);
    }
}
