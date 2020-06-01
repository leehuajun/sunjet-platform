package com.sunjet.backend.modules.asms.entity.basic;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 县区信息实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
//@Table(name = "BasicRegions")
@DiscriminatorValue("COUNTY")
public class CountyEntity extends RegionEntity {
    private static final long serialVersionUID = 9214165516563769460L;

    /**
     * 所属城市
     */
    @Column(length = 32)
    private String cityId;

}
