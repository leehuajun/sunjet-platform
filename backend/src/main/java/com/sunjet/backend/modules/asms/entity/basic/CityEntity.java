package com.sunjet.backend.modules.asms.entity.basic;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 城市信息实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
//@Table(name = "BasicRegions")
@DiscriminatorValue("CITY")
public class CityEntity extends RegionEntity {
    private static final long serialVersionUID = -6481266000109625884L;
    /**
     * 所属省份
     */
    @Column(length = 32)
    private String provinceId;
    /**
     * 城市类别  1. 一类城市   2. 二类城市
     */
    private Integer category;
}
