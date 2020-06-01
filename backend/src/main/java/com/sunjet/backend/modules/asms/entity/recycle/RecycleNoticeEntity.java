package com.sunjet.backend.modules.asms.entity.recycle;

import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.utils.common.DateHelper;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 故障件返回通知单
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmRecycleNotices")
public class RecycleNoticeEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6763932315816003206L;
    @Column(length = 32)
    private String srcDocNo;        //单据编号
    @Column(length = 20)
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    @Column(length = 32)
    private String srcDocID;        // 来源对应单据ID
    @Column(length = 50)
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    @Column(length = 200)
    private String dealerName;      // 服务站名称
    @Column(length = 50)
    private String provinceName;    // 省份
    @Column(length = 200)
    private String comment; //备注

    private Date requestDate = new Date();        //提交时间

    private Date returnDate = DateHelper.nextMonthTenthDate();         // 返回日期要求

    //@OneToMany(cascade = CascadeType.ALL,mappedBy = "recycleNotice")
    //private List<RecycleNoticeItemEntity> recycleNoticeItemEntityList = new ArrayList<>();

}
