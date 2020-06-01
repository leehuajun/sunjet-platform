package com.sunjet.backend.modules.asms.entity.recycle;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 故障件返回通知单清单
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmRecycleNoticeItems")
public class RecycleNoticeItemEntity extends DocEntity {
    private static final long serialVersionUID = 6763942315816003206L;
    //    private PartEntity part;    // 零件
    @Column(length = 50)
    private String partCode;    // 零件号
    @Column(length = 200)
    private String partName;    // 零件编号

    private Integer amount = 0;    //数量

    private Integer backAmount = 0; // 已返回数量

    private Integer currentAmount = 0; // 未返回数量
    @Column(length = 200)
    private String warrantyTime;        // 三包时间 50
    @Column(length = 200)
    private String warrantyMileage;     // 三包里程 50
    @Column(length = 200)
    private String pattern;         // 故障模式
    @Column(length = 200)
    private String reason;          // 换件原因
    @Column(length = 200)
    private String comment; //备注

    @Column(length = 32)
    private String recycleNoticeId;     //故障件返回通知id


    @Column(length = 32)
    private String commissionPartId;   //三包维修配件id
    //@ManyToOne
    //@JoinColumn(name = "RecycleNoticeId")
    //private RecycleNoticeEntity recycleNotice;


    //@OneToMany(cascade = CascadeType.REFRESH, mappedBy = "recycleNoticeItem")
    //private List<RecycleItemEntity> recycleItems = new ArrayList<>();

}
