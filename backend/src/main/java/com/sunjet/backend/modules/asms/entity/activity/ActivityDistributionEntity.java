package com.sunjet.backend.modules.asms.entity.activity;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 活动发布单
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmActivityDistributions")
public class ActivityDistributionEntity extends FlowDocEntity {
    private static final long serialVersionUID = 4896392306966926761L;
    @Column(length = 50)
    private String serviceManager;  // 服务经理
    @Column(length = 50)
    private String dealerCode;      // 服务站编号
    @Column(length = 100)
    private String dealerName;      // 服务站名称
    @Column(length = 200)
    private String comment;          //备注
    @Column(length = 32)
    private String supplyNoticeId;          // 供货通知单Id(调拨单)
    private Boolean canEditSupply = true;      // 是否允许编辑生成调拨通知单,流程提交后，变为false，不允许再生成调拨申请
    private String activityNoticeId;//  服务活动通知单Id
    private String dealer;    // 服务站Id
    private Boolean repair = false;     // 是否已参加维修，默认为false

}
