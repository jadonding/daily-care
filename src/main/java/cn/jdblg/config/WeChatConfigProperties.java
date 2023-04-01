package cn.jdblg.config;

import cn.jdblg.model.ReceiverInfoDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * @author jadonding
 */
@Data
@Configuration
@ConfigurationProperties(prefix = WeChatConfigProperties.PREFIX)
public class WeChatConfigProperties {
    public static final String PREFIX = "jdblg.wechat";
    private String appId;

    private String appSecret;

    private String templateId;

    private List<ReceiverInfoDTO> receiverList;
}
