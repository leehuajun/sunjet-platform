package com.sunjet.backend.modules.asms.entity.activity;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 活动通知
 * <p>
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmActivityNotices")
public class ActivityNoticeEntity extends FlowDocEntity {
    private static final long serialVersionUID = -3966752937369885755L;
    @Column(length = 200)
    private String title;               // 标题
    private Date publishDate = new Date();          // 发布时间  日历控件，选择项，默认当前时间，可改
    @Column(length = 50)
    private String activityType;        // 活动类型
    @Column(length = 200)
    private String noticeFile;          // 活动文件
    private Date startDate = new Date();            // 开始日期
    private Date endDate = new Date();              // 结束时间
    @Column(length = 200)
    private String comment;                         // 备注/活动概述
    private Double perLaberCost = 0.0;        // 单台人工成本
    private Double amountLaberCost = 0.0;     // 人工成本合计
    private Double perPartCost = 0.0;         // 单台配件成本
    private Double amountPartCost = 0.0;      // 配件成本合计
    private Double amount = 0.0;              // 总成本合计

    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false

}
