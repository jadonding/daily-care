package cn.jdblg.util;

import cn.jdblg.config.ApiConfigProperties;
import cn.jdblg.config.WeChatConfigProperties;
import cn.jdblg.model.ReceiverInfoDTO;
import cn.jdblg.model.WeatherDTO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpMapConfigImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @author jadonding
 */
@Slf4j
public class Pusher {

    public static void push(WeChatConfigProperties weChatConfig,
                            ApiConfigProperties apiConfigProperties) throws Exception {
        WxMpDefaultConfigImpl wxStorage = new WxMpMapConfigImpl();
        wxStorage.setAppId(weChatConfig.getAppId());
        wxStorage.setSecret(weChatConfig.getAppSecret());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        List<ReceiverInfoDTO> receiverList = weChatConfig.getReceiverList();
        if (CollectionUtils.isEmpty(receiverList)) {
            log.error("没有设置接收者信息");
            return;
        }
        for (ReceiverInfoDTO receiverInfoDTO : receiverList) {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(receiverInfoDTO.getOpenId())
                .templateId(weChatConfig.getTemplateId())
                //点击模版消息要访问的网址
                //.url("")
                .build();
            //3,如果是正式版发送模版消息，这里需要配置你的信息
            //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
            //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));

            //填写变量信息，比如天气之类的
            JSONObject weather = WeatherUtil.getWeather(receiverInfoDTO.getCity(), apiConfigProperties.getJuheApiKey());
            log.info("天气信息:{}", weather);
            JSONObject realtimeWeather = weather.getJSONObject("realtime");
            JSONArray futureWeatherArray = weather.getJSONArray("future");

            String windDirection = realtimeWeather.getString("direct");
            String windPower = realtimeWeather.getString("power");
            String nowTemperature = realtimeWeather.getString("temperature");

            String todayWeatherString = futureWeatherArray.get(0)
                .toString();
            WeatherDTO weatherDTO = JsonUtil.from(todayWeatherString, WeatherDTO.class);
            String temperature = weatherDTO.getTemperature();
            String[] split = temperature.split("/");

            //接口
            templateMessage.addData(new WxMpTemplateData("date",
                weatherDTO.getDate() + "  " + MemorialDayUtil.getWeekOfDate(new Date()), "#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("weather", weatherDTO.getWeather(), "#00FFFF"));
            templateMessage.addData(new WxMpTemplateData("lowestTemperature", split[0] + "℃" + "", "#173177"));
            templateMessage.addData(new WxMpTemplateData("highestTemperature", split[1] + "", "#FF6347"));
            templateMessage.addData(new WxMpTemplateData("loveWords", SweetWords.getSweetWords(apiConfigProperties.getTianApiKey()), "#FF69B4"));
            templateMessage.addData(new WxMpTemplateData("loveDays",
                MemorialDayUtil.after(receiverInfoDTO.getLoveDay()) + "", "#FF1493"));
            templateMessage.addData(new WxMpTemplateData("birthDays",
                MemorialDayUtil.after(receiverInfoDTO.getBirthday()) + "", "#FFA500"));
            templateMessage.addData(new WxMpTemplateData("goldenSentence", SweetWords.getGoldenSentence() + "", "#C71585"));
            String tips = MemorialDayUtil.getNotes(receiverInfoDTO);
            templateMessage.addData(new WxMpTemplateData("tips", tips, "#FF0000"));
            try {
                log.info("推送消息内容:{}", JsonUtil.toJson(templateMessage));
                String res = wxMpService.getTemplateMsgService()
                    .sendTemplateMsg(templateMessage);
                log.info("推送结果:{}", res);
            } catch (Exception e) {
                log.error("推送失败", e);
            }

        }
    }

}
