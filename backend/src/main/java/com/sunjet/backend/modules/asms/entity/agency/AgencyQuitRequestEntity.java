package com.sunjet.backend.modules.asms.entity.agency;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 合作商退出申请单实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "AgencyQuitRequests")
public class AgencyQuitRequestEntity extends FlowDocEntity {
    private static final long serialVersionUID = 317066571588851599L;
    /**
     * 合作库信息
     */
    //@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    //@JoinColumn(name = "Agency")
    //private AgencyEntity agency;
    private String agency;

    /**
     * 退出原因
     */
    @Column(name = "Reason", length = 1000)
    private String reason;

}
