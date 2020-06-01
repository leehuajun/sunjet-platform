package com.sunjet.backend.modules.asms.entity.basic;


import com.sunjet.backend.base.IdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by lhj on 16/6/30.
 * 单据基础信息实体
 */
@Data
@Entity
@Table(name = "BasicDocumentNo")
public class DocumentNoEntity extends IdEntity {
    private static final long serialVersionUID = 1674940020293536123L;
    /**
     * 单据名称
     */
    @Column(length = 50)
    private String docName;
    /**
     * 单据Key，实体的类名字
     */
    @Column(length = 50)
    private String docKey;
    /**
     * 单据代码 简写 ，长度不定，长度应小于10位, ZLSB 代表质量速报
     */
    @Column(length = 10)
    private String docCode;
    /**
     * // 最后一个单据号的日期部分(8位)  20160801
     */
    @Column(length = 8)
    private String lastNoDate = LocalDate.now().toString().replace("-", "");
    /**
     * 最后一个单据号的流水部分(4位)  0001
     */
    @Column(length = 4)
    private String lastNoSerialNumber = "0000";
}
