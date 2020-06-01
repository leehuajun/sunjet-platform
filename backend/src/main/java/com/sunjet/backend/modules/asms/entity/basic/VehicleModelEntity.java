package com.sunjet.backend.modules.asms.entity.basic;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 配件信息实体
 */
@Data
@Entity
@Table(name = "BasicVehicleModels")
public class VehicleModelEntity extends DocEntity {
    private static final long serialVersionUID = -6092133173560904203L;
    /**
     * 车型代码
     */
    @Column(length = 50, unique = true)
    private String modelCode;
    /**
     * 车型类别代码
     */
    @Column(length = 200)
    private String typeCode;
    /**
     * 车型类别名称
     */
    @Column(length = 200)
    private String typeName;
}
