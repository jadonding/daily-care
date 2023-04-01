package cn.jdblg.util;

import cn.jdblg.model.ReceiverInfoDTO;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jadonding
 */
public class MemorialDayUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 距离date还有多少天
     *
     * @param date 目标日期
     * @return 天数
     */
    public static int before(String date) {
        int day = 0;
        try {
            long time = simpleDateFormat.parse(date)
                .getTime() - System.currentTimeMillis();
            day = (int) (time / 86400000L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


    /**
     * 已经过去date多少天
     *
     * @param date 目标日期
     * @return 天数
     */
    public static int after(String date) {
        int day = 0;
        try {
            long time = System.currentTimeMillis() - simpleDateFormat.parse(date)
                .getTime();
            day = (int) (time / 86400000L) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 特殊日期备注
     *
     * @param receiverInfoDTO {@link ReceiverInfoDTO}
     * @return 备注
     */
    public static String getNotes(ReceiverInfoDTO receiverInfoDTO) {
        // 判断MM-dd是否相同
        StringBuilder sb = new StringBuilder();
        // 生日
        String birthday = receiverInfoDTO.getBirthday();
        String[] split = birthday.split("-");
        String month = split[1];
        String day = split[2];
        Calendar calendar = Calendar.getInstance();
        Date nowDate = new Date();
        calendar.setTime(nowDate);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (month.equals(String.valueOf(currentMonth)) && day.equals(String.valueOf(currentDay))) {
            sb.append(receiverInfoDTO.getBirthdayTips());
        }

        // 恋爱日
        String loveDay = receiverInfoDTO.getLoveDay();
        split = loveDay.split("-");
        month = split[1];
        day = split[2];
        if (month.equals(String.valueOf(currentMonth)) && day.equals(String.valueOf(currentDay))) {
            if (StringUtils.isEmpty(sb)) {
                sb.append(receiverInfoDTO.getLoveDayTips());
            } else {
                sb.append("\r\n")
                    .append(receiverInfoDTO.getLoveDayTips());
            }
        }

        // 计算未来最近一次月经
        String lastMenstruation = receiverInfoDTO.getLastMenstruationDate();
        Integer menstruationCycle = receiverInfoDTO.getMenstruationCycle();

        if (StringUtils.isNotEmpty(lastMenstruation) && menstruationCycle != null) {
            split = lastMenstruation.split("-");
            month = split[1];
            day = split[2];
            calendar.set(Calendar.YEAR, Integer.parseInt(split[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            // + 天数
            calendar.add(Calendar.DAY_OF_MONTH, menstruationCycle);
            // 获取下次月经时间
            Date time = calendar.getTime();
            while (time.before(nowDate)) {
                calendar.add(Calendar.DAY_OF_MONTH, menstruationCycle);
                time = calendar.getTime();
            }
            // 如果下次月经时间和当前时间相差小于7天，则显示下次月经时间
            int before = before(simpleDateFormat.format(time));
            if (before < 7) {
                if (StringUtils.isEmpty(sb)) {
                    sb.append("预计还有")
                        .append(before)
                        .append("天就要来姨妈了，要注意保暖哦");
                } else {
                    sb.append("\r\n")
                        .append("预计还有")
                        .append(before)
                        .append("天就要来姨妈了，要注意保暖哦");
                }
            }
        }

        return sb.toString();
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"尊贵星期日", "冠军星期一", "摸鱼星期二", "希望星期三", "疯狂星期四", "至尊星期五", "尊贵星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
