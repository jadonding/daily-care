package cn.jdblg.util;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.HashMap;

/**
 * 天气工具类
 *
 * @author jadonding
 */
public class WeatherUtil {
    /**
     * 获取指定城市的天气
     *
     * @param city 城市
     * @param key  聚合数据申请的key
     * @return 天气信息
     * @throws Exception 异常
     */
    public static JSONObject getWeather(String city, String key) throws Exception {
        String result = null;
        JSONObject today = new JSONObject();
        HashMap<String, String> params = new HashMap<>();
        params.put("city", city);
        params.put("key", key);
        result = HttpClientUtil.get("http://apis.juhe.cn/simpleWeather/query", params);
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getString("reason")
            .equals("查询成功!")) {
            today = jsonObject.getJSONObject("result");
        }
        return today;
    }
}
