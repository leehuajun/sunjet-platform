package com.sunjet.dto.asms.asm;

import com.sunjet.dto.system.base.DocInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by SUNJET_WS on 2017/7/10.
 * 外出子行信息VO
 */
@Data
public class GoOutInfo extends DocInfo implements Serializable {

    private Integer rowNum;     // 行号
    private String place;    // 外出地点
    private Double mileage = 0.0;     // 单向里程
    private Double tranCosts = 0.0;     // 交通费用
    private Double trailerMileage = 0.0;     // 拖车里程
    private Double trailerCost = 0.0;     // 拖车费用
    private int outGoNum = 0;    // 外出人数
    private Double outGoDay = 0.0;    // 外出天数
    private Double personnelSubsidy = 0.0;    // 人员补贴
    private Double nightSubsidy = 0.0;    // 住宿补贴
    private Double timeSubsidy = 0.0;    // 外出工时费用
    private Double goOutSubsidy = 0.0;    // 外出补贴费用
    private Double amountCost = 0.0;      // 行汇总金额
    private String outGoPicture;    //外出凭证照片
    private String comment;     // 备注

    private String firstMaintenanceId;//首保服务单id

    private String warrantyMaintenance;   //三包单Id


}
