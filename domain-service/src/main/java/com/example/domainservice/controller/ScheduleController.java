package com.example.domainservice.controller;

import com.example.domainservice.model.Schedule;
import com.example.domainservice.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // Получить весь список
    @GetMapping
    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    // Получить по id
    @GetMapping("/{id}")
    public Schedule getById(@PathVariable String id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    // Создать новый
    @PostMapping
    public Schedule create(@RequestBody Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // Обновить существующий
    @PutMapping("/{id}")
    public Schedule update(@PathVariable String id, @RequestBody Schedule schedule) {
        schedule.setId(id);
        return scheduleRepository.save(schedule);
    }

    // Удалить
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        scheduleRepository.deleteById(id);
    }
}