package com.example.gateway.dto;

public class UpdateMessage {
    private String id;
    private Object scheduleDto; // или ScheduleDto, если есть класс

    public UpdateMessage() {
    }

    public UpdateMessage(String id, Object scheduleDto) {
        this.id = id;
        this.scheduleDto = scheduleDto;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Object getScheduleDto() {
        return scheduleDto;
    }
    public void setScheduleDto(Object scheduleDto) {
        this.scheduleDto = scheduleDto;
    }
}