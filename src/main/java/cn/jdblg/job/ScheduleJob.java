package cn.jdblg.job;

import cn.jdblg.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author jadonding
 */
@Component
@RequiredArgsConstructor
public class ScheduleJob {
    private final PushService pushService;

    @Scheduled(cron = "${jdblg.cron:0 30 8 * * ?}")
    public void goodMorning() {
        pushService.push();
    }
}

