package cn.jdblg.job;

import cn.jdblg.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author jadonding
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleJob {
    private final PushService pushService;

    @Scheduled(cron = "${jdblg.cron:0 30 8 * * ?}")
    public void goodMorning() {
        log.info("开始执行定时推送任务");
        pushService.push();
    }
}

