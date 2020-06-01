package com.sunjet.backend.modules.asms.entity.recycle;

import com.sunjet.backend.base.DocEntity;
import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 待返回故障件
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmRecycleItems")
public class RecycleItemEntity extends DocEntity {

    private static final long serialVersionUID = 6763932315816003206L;
    @Column(length = 200)
    private String logisticsNum;    // 物流单号
    //private Date requestDate;       // 开单时间
    @Column(length = 32)
    private String srcDocNo;        //单据编号
    @Column(length = 20)
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、

    //@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @Column(name = "PartId")
    private PartEntity part;        // 零件

    @Column(length = 50)
    private String partCode;        // 零件号
    @Column(length = 200)
    private String partName;        // 零件名称
    @Column(length = 200)
    private String warrantyTime;        // 三包时间 50
    @Column(length = 200)
    private String warrantyMileage;     // 三包里程 50

    private Integer waitAmount = 0;           // 待返数量

    private Integer backAmount = 0;          // 本次返回数量

    private Date returnDate = new Date();   //要求返回时间

    private Integer acceptAmount = 0;        // 收货数量
    @Column(length = 200)
    private String pattern;         // 故障模式
    @Column(length = 200)
    private String reason;          // 换件原因
    @Column(length = 200)
    private String comment;     // 备注

    @Column(length = 32)
    private String recycle; // 故障件返回单
    //@ManyToOne
    //@JoinColumn(name="Recycle")
    //private RecycleEntity recycleEntity;

    @Column(length = 32)
    private String noticeItemId;    //返回单子行id
    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "NoticeItemId")
    //private RecycleNoticeItemEntity recycleNoticeItem;      // 返回通知单的行对象

}
