package com.example.gateway.dto;

import lombok.Data;

@Data
public class UpdateScheduleDto {
    private String dayOfWeek;
    private String startTime;
    private String subject;
    private String teacher;
}