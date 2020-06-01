package com.sunjet.backend.modules.asms.entity.supply;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 配件调拨单/供货通知单
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmSupplyNotices")
public class SupplyNoticeEntity extends FlowDocEntity {
    private static final long serialVersionUID = 3390052374643452540L;
    @Column(length = 32)
    private String docType;     // 单据类型
    @Column(length = 20)
    private String srcDocNo;     // 单据编号
    @Column(length = 20)
    private String srcDocType;   // 来源类型：三包服务单、首保服务单、服务活动单、
    @Column(length = 32)
    private String srcDocID;     // 来源对应单据ID
    @Column(length = 50)
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    @Column(length = 100)
    private String dealerName;      // 服务站名称
    @Column(length = 50)
    private String provinceName;    // 省份
    @Column(length = 50)
    private String serviceManager;   // 服务经理
    @Column(length = 50)
    private String operator;         // 经办人
    @Column(length = 20)
    private String operatorPhone;    // 联系电话
    @Column(length = 50)
    private String receive;         //收货人
    //    private String receivePhone;   // 收货人联系电话
    @Column(length = 200)
    private String dealerAdderss;       // 服务站收货地址
    @Column(length = 200)
    private String comment; //备注

    @Column(length = 32)
    private String agencyCode;  // 经销商名称
    @Column(length = 32)
    private String agencyName;  // 经销商名称
    @Column(length = 32)
    private String agencyId;  // 经销商Id


    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "SupplyNoticeId")
    //private List<SupplyNoticeItemEntity> items = new ArrayList<>();     // 物料列表
}
