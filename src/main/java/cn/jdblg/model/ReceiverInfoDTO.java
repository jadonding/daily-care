package cn.jdblg.model;

import lombok.Data;

/**
 * 接收者信息
 *
 * @author jadonding
 */
@Data
public class ReceiverInfoDTO {
    private String openId;
    private String city;
    private String birthday;
    private String birthdayTips;
    private String loveDay;
    private String loveDayTips;

    /**
     * 最近一次姨妈时间
     */
    private String lastMenstruationDate;

    /**
     * 姨妈周期
     */
    private Integer menstruationCycle;
}
