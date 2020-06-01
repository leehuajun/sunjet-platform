package com.sunjet.backend.modules.asms.entity.basic;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Created by lhj on 16/6/30.
 * <p>
 * 合作商对应的省份 关联关系表
 * 关系： 多对多
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicAgenciesRegion")
public class AgencyWithRegionEntity {

    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "ObjId", length = 32, nullable = false)
    private String objId;//主键

    /**
     * 合作商Id
     */
    @Column(name = "agencyId", length = 32, nullable = false)
    private String agencyId;

    /**
     * 省份Id
     */
    @Column(name = "provinceId", length = 32, nullable = false)
    private String provinceId;

}