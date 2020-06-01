package com.sunjet.backend.modules.asms.entity.basic;

import com.sunjet.backend.base.DocEntity;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 维修项目 主实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicMaintains")
public class MaintainEntity extends DocEntity {
    private static final long serialVersionUID = -4637484257477614557L;
    /**
     * 维修项目编号
     */
    @Column(name = "Code", length = 20, unique = true)
    private String code;
    /**
     * 维修项目名称
     */
    @Column(name = "Name", length = 50)
    private String name;
    /**
     * 工时定额
     */
    private Double workTime;
    /**
     * 是否索赔
     */
    private Boolean claim = true;

    private String vehicleModelId;
    private String vehicleModelName;
    private String vehicleSystemId;
    private String vehicleSystemName;
    private String vehicleSubSystemId;
    private String vehicleSubSystemName;

    /**
     * 备注
     */
    @Column(length = 500)
    private String comment;

}
