package cn.jdblg.config;

import lombok.Data;

/**
 * @author jadonding
 */
@Data
public class HttpClientConnectionConfig {

    /**
     * 最大连接数
     */
    private Integer maxConnectTotal = 10;

    /**
     * 最大路由数量
     */
    private Integer maxPerRoute = 2;

    /**
     * 连接超时时间
     */
    private Long connectTimeout = 6000L;

    /**
     * 读取超时时间
     */
    private Integer readTimeout = 6000;
}
