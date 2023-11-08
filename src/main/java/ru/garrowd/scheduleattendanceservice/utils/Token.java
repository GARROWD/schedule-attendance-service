package ru.garrowd.scheduleattendanceservice.utils;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.garrowd.scheduleattendanceservice.utils.enums.JwtClaims;

import java.util.List;

public class Token {
    public static String get(JwtAuthenticationToken token, JwtClaims attribute){
        return (String) token.getTokenAttributes().get(attribute.getValue());
    }

    public static List<String> getList(JwtAuthenticationToken token, JwtClaims attribute){
        return (List<String>) token.getTokenAttributes().get(attribute.getValue());
    }
}
