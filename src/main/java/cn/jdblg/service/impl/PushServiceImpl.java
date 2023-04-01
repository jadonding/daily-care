package cn.jdblg.service.impl;

import cn.jdblg.config.ApiConfigProperties;
import cn.jdblg.config.WeChatConfigProperties;
import cn.jdblg.service.PushService;
import cn.jdblg.util.Pusher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author jadonding
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PushServiceImpl implements PushService {
    private final WeChatConfigProperties weChatConfigProperties;
    private final ApiConfigProperties apiConfigProperties;

    @Override
    public void push() {
        try {
            Pusher.push(weChatConfigProperties, apiConfigProperties);
        } catch (Exception e) {
            log.error("推送失败", e);
            throw new RuntimeException(e);
        }
    }


}
