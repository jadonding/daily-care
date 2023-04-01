package cn.jdblg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jadonding
 */
@Data
@Configuration
@ConfigurationProperties(prefix = ApiConfigProperties.PREFIX)
public class ApiConfigProperties {
    public static final String PREFIX = "jdblg.api-config";
    private String juheApiKey;
    private String tianApiKey;
}
