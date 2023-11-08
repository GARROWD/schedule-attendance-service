package ru.garrowd.scheduleattendanceservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@OpenAPIDefinition
@EnableCaching
public class ScheduleAttendanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleAttendanceServiceApplication.class, args);
	}

}
