package com.sunjet.backend.modules.asms.entity.basic;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 维修项目子项实体
 */
@Data
@Entity
@Table(name = "BasicVehicleTypes")
public class MaintainTypeEntity extends DocEntity {
    private static final long serialVersionUID = 2002478285473675298L;
    /**
     * 类型名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 父级类型的id
     */
    @Column(name = "parentId")
    private String parentId;
    /**
     * 描述
     */
    @Column(name = "comment")
    private String comment;
}
