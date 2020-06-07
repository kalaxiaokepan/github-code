package com.github.code.conf;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 多线程执行定时任务
 */
@Configurable
public class ScheduleConfig implements SchedulingConfigurer {

    private static final int FIVE = 5;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(FIVE));
    }
}
