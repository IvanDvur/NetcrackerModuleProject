package com.netcracker.SendManager.service.schedulers;

import dto.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class OrderScheduler {

    private TaskScheduler taskScheduler;
    private Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
    private Logger log = LoggerFactory.getLogger(OrderScheduler.class);
    public static Integer schedulerLoad = 0;
    public OrderScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void scheduleATask(Schedule schedule, Runnable tasklet) {
        if (!jobsMap.containsKey(schedule.getId().toString())) {
            String jobId = schedule.getId().toString();
            LocalDateTime timeToSend = schedule.getTimeToSend();
            String hour = Integer.toString(timeToSend.getHour());
            String minute = Integer.toString(timeToSend.getMinute());
            String second = Integer.toString(timeToSend.getSecond());
            String cronExp = second + " " + minute + " " + hour + " * " + "* " + "*";
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExp));
            jobsMap.put(jobId, scheduledTask);
            schedulerLoad = jobsMap.size();
            log.info("Placing order in schedule {}",schedule);
        }
    }

    public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(jobId);
            schedulerLoad = jobsMap.size();
        }
    }
}
