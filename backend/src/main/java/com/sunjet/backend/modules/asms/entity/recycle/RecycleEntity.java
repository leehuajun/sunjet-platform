package com.sunjet.backend.modules.asms.entity.recycle;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 故障件返回单
 * Created by lhj on 16/9/17.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmRecycleDocs")
public class RecycleEntity extends FlowDocEntity {
    private static final long serialVersionUID = 7507696661216826485L;

    @Column(length = 200)
    private String operator;        // 经办人
    @Column(length = 200)
    private String operatorPhone;   // 联系电话
    @Column(length = 200)
    private String serviceManager;  // 服务经理
    @Column(length = 200)
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    @Column(length = 200)
    private String dealerName;      // 服务站名称
    @Column(length = 200)
    private String provinceName;    // 省份
    @Column(length = 200)
    private String logistics;       // 物流名称
    @Column(length = 200)
    private String logisticsNum;    // 物流单号
    @Column(length = 200)
    private String recyclefile;     // 故障件附件
    @Column(length = 200)
    private String logisticsfile;   // 物流附件
    @Column(length = 200)
    private String recyclefileName;     // 故障件附件显示
    @Column(length = 200)
    private String logisticsfileName;   // 物流附件显示

    private Double transportExpense = 0.0;  // 运输费用

    private Double otherExpense = 0.0;      // 其他费用

    private Double expenseTotal = 0.0;      // 费用合计
    private Date arriveDate; //预计送达
    private boolean received;//是否收货
    private Date rCVDate; //收货时间


    private boolean settlement = false;//是否结算

    @Column(length = 200)
    private String comment;   //备注


    //@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    //@JoinColumn(name="Recycle")
    //private List<RecycleItemEntity> items= new ArrayList<>();     // 清单

}
