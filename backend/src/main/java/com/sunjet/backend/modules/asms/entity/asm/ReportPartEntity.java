package com.sunjet.backend.modules.asms.entity.asm;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 速报配件子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmReportParts")
public class ReportPartEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;

    //private Integer rowNum;     // 行号

    //@ManyToOne(cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "partId")
    private String part_id;    // 零件实体

    private Integer amount = 1;     // 数量
    @Column(length = 200)
    private String fault;       // 故障模式
    @Column(length = 200)
    private String comment;     // 备注

    private String qrId; //质量速报id

    private String crId;    //费用速报id

}
