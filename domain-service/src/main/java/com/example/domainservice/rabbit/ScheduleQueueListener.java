package com.example.domainservice.rabbit;

import com.example.domainservice.model.Schedule;
import com.example.domainservice.repository.ScheduleRepository;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ScheduleQueueListener {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // слушаем одну очередь "schedule-queue".
    // routingKey= "create", "update", "delete" и т.д. (если Gateway посылает их).
    @RabbitListener(queues = "schedule-queue")
    public void processMessage(@Header("amqp_receivedRoutingKey") String routingKey,
                               @Payload Object body) {

        switch (routingKey) {
            case "create":
                // body -> ScheduleDto (или Map). Преобразовать и сохранить
                handleCreate(body);
                break;

            case "update":
                handleUpdate(body);
                break;

            case "delete":
                handleDelete(body);
                break;

            default:
                System.out.println("Unknown routingKey: " + routingKey);
        }
    }

    private void handleCreate(Object body) {
        // мапим object -> Schedule (зависит от того, что Gateway отправляет)
        if (body instanceof Schedule) {
            Schedule schedule = (Schedule) body;
            scheduleRepository.save(schedule);
            System.out.println("Created schedule: " + schedule);
        } else {
            System.out.println("Cannot cast body to Schedule: " + body);
        }
    }

    private void handleUpdate(Object body) {
        // Аналогично parse, findById, update...
        if (body instanceof UpdateMessage) {
            UpdateMessage msg = (UpdateMessage) body;
            // Найти scheduleRepository.findById(msg.getId())
            // set fields, save...
            System.out.println("Updated schedule with id=" + msg.getId());
        }
    }

    private void handleDelete(Object body) {
        // body может быть просто String id
        if (body instanceof String) {
            String id = (String) body;
            scheduleRepository.deleteById(id);
            System.out.println("Deleted schedule with id=" + id);
        } else {
            System.out.println("Cannot cast body to String for delete: " + body);
        }
    }
}

@Data
class UpdateMessage {
    private String id;
    private Schedule schedule;
}