package com.sunjet.backend.modules.asms.entity.flow;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-03-22 15:51
 * @description: 说明
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "flowComment")
public class CommentEntity {
    /**
     * 自增长id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    /**
     * 流程实例id
     */
    @Column(name = "FlowinstId", length = 50)
    private String flowInstanceId;
    /**
     * 审批人id
     */
    @Column(name = "UserId", length = 50)
    private String userId;
    /**
     * 审批人姓名
     */
    @Column(name = "Username", length = 50)
    private String username;
    /**
     * 审批时间
     */
    @Column(name = "DoDate")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date doDate;
    /**
     * 审批结果
     */
    @Column(name = "Result", length = 200)
    private String result;
    /**
     * 审批意见
     */
    @Column(name = "Message", length = 2000)
    private String message;

}
