package com.example.gateway.controller;

import com.example.gateway.dto.UpdateMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class GatewayRestController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AmqpTemplate rabbitTemplate;

    @Value("${domain.service.url}")
    private String domainServiceUrl;

    private static final String SCHEDULE_QUEUE = "schedule-queue";

    // Внедряем AmqpTemplate через конструктор
    public GatewayRestController(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // СИНХРОННО (REST) + КЕШИРОВАНИЕ REDIS

    @Cacheable(value = "schedules")
    @GetMapping("/schedule")
    public Object[] getAllSchedules() {
        String url = domainServiceUrl + "/schedule";
        Object[] schedules = restTemplate.getForObject(url, Object[].class);
        return schedules;
    }

    @Cacheable(value = "schedules")
    @GetMapping("/schedule/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        String url = domainServiceUrl + "/schedule/" + id;
        return restTemplate.getForEntity(url, Object.class);
    }

    // АСИНХРОННО ЧЕРЕЗ RABBITMQ

    @PostMapping("/schedule")
    public ResponseEntity<?> create(@RequestBody Object scheduleDto) {
        rabbitTemplate.convertAndSend(SCHEDULE_QUEUE, "create", scheduleDto);
        return ResponseEntity.accepted().build(); // 202 Accepted
    }

    @PutMapping("/schedule/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Object scheduleDto) {
        rabbitTemplate.convertAndSend(SCHEDULE_QUEUE, "update",
                new UpdateMessage(id, scheduleDto));
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        rabbitTemplate.convertAndSend(SCHEDULE_QUEUE, "delete", id);
        return ResponseEntity.noContent().build();
    }
}