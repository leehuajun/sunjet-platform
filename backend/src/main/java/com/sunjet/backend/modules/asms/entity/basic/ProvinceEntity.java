package com.sunjet.backend.modules.asms.entity.basic;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 省份信息实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
//@Table(name = "BasicRegions")
@DiscriminatorValue("PROVINCE")
public class ProvinceEntity extends RegionEntity {
    private static final long serialVersionUID = 5723499113546627518L;
    /**
     * 是否严寒省份，默认为：非严寒省份
     */

    private Boolean cold = false;  // 是否严寒省份，默认为：非严寒省份
}
