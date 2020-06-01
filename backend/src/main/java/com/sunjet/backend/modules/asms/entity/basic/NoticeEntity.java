package com.sunjet.backend.modules.asms.entity.basic;


import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 公告
 * <p>
 * Created by lhj on 16/10/23.
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "BasicNotices")
public class NoticeEntity extends DocEntity {
    private static final long serialVersionUID = -7942079054161894365L;
    /**
     * 公告标题
     */
    @Column(length = 200)
    private String title;
    /**
     * 发布时间
     */

    private Date publishDate = new Date();
    /**
     * 发布人
     */
    @Column(length = 50)
    private String publisher;
    /**
     * 公告内容
     */
    @Column(length = 10000)
    private String content;
    /**
     * 附件原始名称
     */
    @Column(length = 200)
    private String fileOriginName;
    /**
     * 附件真实路径
     */
    @Column(length = 200)
    private String fileRealName;

    /**
     * 是否置顶
     */
    private Boolean isTop = false;

}
