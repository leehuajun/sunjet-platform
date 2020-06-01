package com.sunjet.backend.modules.asms.entity.recycle.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/8/10.
 * 故障件返回单
 */
@Data
@Entity
@Immutable
@Subselect("SELECT ( SELECT GROUP_CONCAT(ari.src_doc_no) FROM asm_recycle_items AS ari WHERE ari.recycle = ard.obj_id ) AS src_doc_no , ard.* FROM `asm_recycle_docs` ard")
public class RecycleView {

    @Id
    private String objId;   //主键id

    private String docNo;   //单号

    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出

    private String dealerName;      // 服务站名称

    private Date createdTime;   // 提交时间

    private String logistics;       // 物流名称

    private String logisticsNum;    // 物流单号

    private String submitterName;       // 提交人姓名

    private Integer status;         // 表单状态

    private String processInstanceId;   // 流程实例Id

    private String serviceManager;  // 服务经理

    private String srcDocNo; // 来源单据
}
