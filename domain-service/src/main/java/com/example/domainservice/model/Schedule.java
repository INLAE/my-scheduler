package com.example.domainservice.model;

import lombok.Data;   // если используем Lombok
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "schedule")
public class Schedule {
    @Id
    private String id;

    private String dayOfWeek;
    private String startTime;
    private String subject;
    private String teacher;
}