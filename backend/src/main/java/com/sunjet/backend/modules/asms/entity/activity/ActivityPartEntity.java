package com.sunjet.backend.modules.asms.entity.activity;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务活动配件子行
 * Created by lhj on 16/9/15.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AsmActivityParts")
public class ActivityPartEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;


    @Column(length = 32)
    private String activityNoticeId;  //活动通知id
    private String partId;    //配件id
    private Integer amount = 1;     // 数量
    @Column(length = 200)
    private String comment;     // 备注


}
