package com.sunjet.backend.modules.asms.entity.basic;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 行政关系基础信息实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicRegions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "R")
public class RegionEntity extends DocEntity {
//    private static final long serialVersionUID = -7531693176868504168L;
    /**
     * 编号
     */
    @Column(name = "Code", length = 10, nullable = false)
    private String code;
    /**
     * 名称
     */
    @Column(name = "Name", length = 50, nullable = false)
    private String name;
}
