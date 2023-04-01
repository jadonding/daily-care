package cn.jdblg.model;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * @author jadonding
 */
@Data
public class WeatherDTO {
    private String date;
    private JSONObject wid;
    private String temperature;
    private String weather;
    private String direct;
}
