package com.example.scheduler.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    @Scheduled(fixedRate = 5000) // 每5秒执行一次（任务开始时间间隔）
    public void taskFixedRate() {
        logger.info("固定速率执行任务: {}",  dateFormat.format(new Date()));
    }

    @Scheduled(fixedDelay = 10000) // 上次任务执行完成后间隔10秒
    public void taskFixedDelay() {
        logger.info("固定延迟执行任务: {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0 0 1 * * ?") // 使用Cron表达式
    public void taskDailyAt1AM() {
        logger.info("每天凌晨1点执行任务{}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "${spring.quartz.cron.expression}") // 动态引用字段
    public void taskWithDynamicSchedule() {
        logger.info("动态调度任务执行{}", dateFormat.format(new Date()));
    }
}