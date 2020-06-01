package com.sunjet.backend.system.entity;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by lhj on 2015/9/6.
 * 系统配置信息实体
 */
@Data
//@Builder()
//@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SysConfigs")
public class ConfigEntity extends DocEntity {
    private static final long serialVersionUID = 7374987575666673359L;
    /**
     * 配置项Key
     */
    @Column(name = "ConfigKey", length = 50, unique = true)
    private String configKey;
    /**
     * 配置项Value
     */
    @Column(name = "ConfigName", length = 200)
    private String configValue;
    /**
     * 配置项默认Value
     */
    @Column(name = "ConfigDefaultValue", length = 200)
    private String configDefaultValue;
    /**
     * 配置项备注
     */
    @Column(name = "Comment", length = 200)
    private String comment;

}
