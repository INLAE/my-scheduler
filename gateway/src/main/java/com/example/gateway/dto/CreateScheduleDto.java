package com.example.gateway.dto;

import lombok.Data;

@Data
public class CreateScheduleDto {
    private String dayOfWeek;
    private String startTime;
    private String subject;
    private String teacher;

}