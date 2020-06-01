package com.sunjet.backend.modules.asms.entity.basic;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hxf on 2015/11/05.
 * 资源实体
 */
@Data
@Entity
@Table(name = "BasicVehiclePlatforms")
public class VehiclePlatformEntity extends DocEntity {
    private static final long serialVersionUID = 2594693976909631899L;
    /**
     * 资源名称
     */
    @Column(name = "Name", length = 100)
    private String name;

}
