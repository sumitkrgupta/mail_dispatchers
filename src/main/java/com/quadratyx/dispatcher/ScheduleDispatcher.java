package com.quadratyx.dispatcher;

import com.quadratyx.dispatcher.service.EmailDispatcher;
import com.quadratyx.dispatcher.service.impl.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"com.quadratyx"})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"com.quadratyx.dispatcher.service"})
@EntityScan(basePackages = {"com.quadratyx.dispatcher.entity", "com.quadratyx.dispatcher"})
@EnableScheduling
public class ScheduleDispatcher  {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleDispatcher.class);

    @Value("${thread.corePoolSize}")
    private int corePoolSize;
    @Value("${thread.maxPoolSize}")
    private int maxPoolSize;
    @Value("${Schedule.corePoolSize}")
    private int scheduleCorePoolSize;
    @Value("${db.connection.error.mail}")
    private String dbFailMail;

    private static boolean emailSent = false;
    private final EmailServiceImpl emailService;
    private final ApplicationContext appContext;
    private final EmailDispatcher emailDispatcher;

    public ScheduleDispatcher(EmailServiceImpl emailService, ApplicationContext appContext, EmailDispatcher emailDispatcher) {
        this.emailService = emailService;
        this.appContext = appContext;
        this.emailDispatcher = emailDispatcher;
    }

    @Scheduled(cron = "${spring.schedule.mail.dispatcher}",zone = "${spring.schedule.dispatcher.timeZone}")
    public void startMailDispatcher() {
        logger.info("Starting Mail Dispatcher.......................");
        DataSourceHealthIndicator dshi = appContext.getBean(DataSourceHealthIndicator.class);
        Health health = dshi.health();
        Status status = health.getStatus();
        logger.info(status.toString());

        if ("DOWN".equals(status.getCode())) {
            if (!emailSent) {
                sendNotification();
            }
        } else {
            emailSent = false;
            Instant startTime=Instant.now();
            emailDispatcher.emailDispatcher();
            Instant endTime = Instant.now();
           logger.info("Time Taken Email Dispatcher: " + Duration.between(startTime, endTime));
        }
    }

    private void sendNotification() {
        String message = "Database connection lost";
        logger.error(message);
        try {
            emailService.sendSimpleMessage(dbFailMail,"DB Connection Fail",message);
            emailSent = true;
        } catch ( MailException ex ) {
            logger.error("'Database connection lost' email notification failed");
        }
    }

    @Bean
    public Executor getTaskExecutor() {
        return Executors.newScheduledThreadPool(scheduleCorePoolSize);
    }
    @Bean
    public TaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor=new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(corePoolSize);
        threadPoolExecutor.setMaxPoolSize(maxPoolSize);
        return threadPoolExecutor;
    }
}
