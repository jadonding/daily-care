package cn.jdblg.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 对接天行数据的土味情话api
 *
 * @author jadonding
 */
public class SweetWords {

    /**
     * 对接天行数据的土味情话api
     */
    private static final String URL = "http://api.tianapi.com/saylove/index";
    private static final String NAME = "老婆";
    /**
     * logger
     **/
    private static final Logger logger = LoggerFactory.getLogger(SweetWords.class);
    private static List<String> goldenSentenceList = new ArrayList<>();

    /**
     * 载入金句库
     */
    static {
        try (BufferedReader br =
                 new BufferedReader(new InputStreamReader(Objects.requireNonNull(SweetWords.class.getClassLoader()
            .getResourceAsStream("goldenSentence.txt"))))) {
            StringBuilder str = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                if (!StringUtils.isEmpty(temp)) {
                    str.append("\r\n")
                        .append(temp);
                } else {
                    goldenSentenceList.add(str.toString());
                    str = new StringBuilder();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String getSweetWords(String key) {
        //默认彩虹屁
        String str = "鱼在水里，你在我心里";
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("key", key);
            String result = HttpClientUtil.get(URL, params);
            JSONObject jsonObject = new JSONObject(result.replace("XXX", NAME));
            if (jsonObject.getInt("code") == 200) {
                str = jsonObject.getJSONArray("newslist")
                    .getJSONObject(0)
                    .getString("content");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return str;
    }

    public static String getGoldenSentence() {
        Random random = new Random();
        return goldenSentenceList.get(random.nextInt(goldenSentenceList.size()));
    }
}
