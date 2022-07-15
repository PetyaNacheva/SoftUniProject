package MyProjectGradle.scheduler;

import MyProjectGradle.service.LogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogClearScheduler {
    private final LogService logService;

    public LogClearScheduler(LogService logService) {
        this.logService = logService;
    }

    //every 1-st at midnight
    @Scheduled(cron = "0 0 1 * *")
    public void logClear() {
        logService.logClear();
    }
}