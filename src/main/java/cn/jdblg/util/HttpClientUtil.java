package cn.jdblg.util;

import cn.jdblg.config.HttpClientConnectionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jadonding
 **/
@Slf4j
@RequiredArgsConstructor
public class HttpClientUtil {

    private static final HttpClientConnectionConfig httpClientConnectionConfig = new HttpClientConnectionConfig();
    private static CloseableHttpClient httpClient;

    static {
        init();
    }

    private static void init() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(SocketConfig.custom()
            .setSoTimeout(httpClientConnectionConfig.getReadTimeout())
            .build());
        // 连接池最大生成连接数
        connectionManager.setMaxTotal(httpClientConnectionConfig.getMaxConnectTotal());
        connectionManager.setDefaultMaxPerRoute(httpClientConnectionConfig.getMaxPerRoute());
        // 默认设置route最大连接数
        connectionManager.setDefaultMaxPerRoute(httpClientConnectionConfig.getMaxPerRoute());
        //Http客户端
        HttpClientBuilder builder = HttpClientBuilder.create();
        //最大连接数量
        builder.setMaxConnTotal(httpClientConnectionConfig.getMaxConnectTotal());
        //最大路由数量,每一轮最大并行连接的数量,比如一台服务器就是10,两台服务器就是5，5
        builder.setMaxConnPerRoute(httpClientConnectionConfig.getMaxPerRoute());
        builder.setConnectionTimeToLive(httpClientConnectionConfig.getConnectTimeout(), TimeUnit.MILLISECONDS);
        builder.setConnectionManager(connectionManager);
        httpClient = builder.build();
    }

    public static String get(String url, Map<String, String> params) throws URISyntaxException {
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(buildUri(url, params));
        return execute(httpGet);
    }


    public static String post(String url, Map<String, String> params, Map<String, String> headers, String jsonData) throws UnsupportedEncodingException, URISyntaxException {
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(buildUri(url, params));
        if (!MapUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> httpPost.addHeader(new BasicHeader(key, value)));
        }
        if (!StringUtils.isEmpty(jsonData)) {
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpPost.setEntity(new StringEntity(jsonData));
        }
        return execute(httpPost);
    }


    public static String post(String url, Map<String, String> headers, String jsonData) throws UnsupportedEncodingException, URISyntaxException {
        return post(url, null, null, jsonData);
    }


    public static String post(String url, Map<String, String> params) throws UnsupportedEncodingException,
        URISyntaxException {
        return post(url, params, null, null);
    }


    public static String post(String url, String jsonData) throws UnsupportedEncodingException, URISyntaxException {
        return post(url, null, jsonData);
    }

    /**
     * 构建Url
     *
     * @param url    url
     * @param params 参数
     * @return 返回URI
     */
    private static URI buildUri(String url, Map<String, String> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (!MapUtils.isEmpty(params)) {
            params.forEach(uriBuilder::addParameter);
        }
        return uriBuilder.build();
    }

    private static String execute(HttpUriRequest request) {
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException("HTTP连接失败", e);
        }

        HttpEntity entity = response.getEntity();
        if (response.getStatusLine()
            .getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("响应码为 " + response.getStatusLine()
                .getStatusCode() + ", 异常信息为 " + response.getStatusLine()
                .getReasonPhrase());
        }
        try {
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            throw new RuntimeException("HTTP响应结构异常", e);
        }
    }

}
