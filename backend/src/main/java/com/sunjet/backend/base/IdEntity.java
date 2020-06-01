package com.sunjet.backend.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 主键实体
 */
//@Data
@MappedSuperclass
public class IdEntity implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "ObjId", length = 32, nullable = false)
    private String objId;

    @Getter
    @Setter
    @Column(name = "Enabled")
    private Boolean enabled = false;
}
