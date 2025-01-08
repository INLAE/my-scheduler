package com.example.gateway.controller;

import com.example.gateway.model.Schedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${domain.url}")
    private String domainServiceUrl;
    // Ожидаем, что domainServiceUrl = http://domain-service:9090
    // (см. application.properties)

    /**
     * Получить список всех расписаний:
     * GET /api/schedule -> проксируем GET /schedule (Domain-сервис)
     */
    @GetMapping("/schedule")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        String url = domainServiceUrl + "/schedule"; // http://domain-service:9090/schedule

        ResponseEntity<Schedule[]> response =
                restTemplate.getForEntity(url, Schedule[].class);

        // Преобразуем массив в список:
        List<Schedule> schedules = Arrays.asList(response.getBody() != null ? response.getBody() : new Schedule[0]);
        return new ResponseEntity<>(schedules, response.getStatusCode());
    }

    /**
     * Получить расписание по ID:
     * GET /api/schedule/{id} -> проксируем GET /schedule/{id} (Domain-сервис)
     */
    @GetMapping("/schedule/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable String id) {
        String url = domainServiceUrl + "/schedule/" + id;

        ResponseEntity<Schedule> response =
                restTemplate.getForEntity(url, Schedule.class);

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    /**
     * Создать новое расписание:
     * POST /api/schedule -> проксируем POST /schedule (Domain-сервис)
     */
    @PostMapping("/schedule")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        String url = domainServiceUrl + "/schedule";

        ResponseEntity<Schedule> response =
                restTemplate.postForEntity(url, schedule, Schedule.class);

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    /**
     * Обновить расписание:
     * PUT /api/schedule/{id} -> проксируем PUT /schedule/{id} (Domain-сервис)
     */
    @PutMapping("/schedule/{id}")
    public ResponseEntity<Void> updateSchedule(@PathVariable String id,
                                               @RequestBody Schedule schedule) {
        String url = domainServiceUrl + "/schedule/" + id;

        // В RestTemplate нет отдельного метода putForEntity, используем put(...)
        restTemplate.put(url, schedule);

        // Domain-сервис вернёт 200/OK (или 204/No Content).
        return ResponseEntity.ok().build();
    }

    /**
     * Удалить расписание:
     * DELETE /api/schedule/{id} -> проксируем DELETE /schedule/{id} (Domain-сервис)
     */
    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
        String url = domainServiceUrl + "/schedule/" + id;

        restTemplate.delete(url);

        return ResponseEntity.noContent().build();
    }
}