package cn.jdblg.controller;


import cn.jdblg.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jadonding
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PushController {

    private final PushService pushService;

    /**
     * 微信测试账号推送
     */
    @GetMapping("/push")
    public void push() {
        pushService.push();
    }
}