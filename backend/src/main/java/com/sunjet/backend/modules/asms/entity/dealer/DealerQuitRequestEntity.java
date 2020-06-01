package com.sunjet.backend.modules.asms.entity.dealer;

import com.sunjet.backend.base.FlowDocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 服务站退出申请单实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "DealerQuitRequests")
public class DealerQuitRequestEntity extends FlowDocEntity {
    private static long serialVersionUID = 317066571588851599L;
    /**
     * 服务站ID
     */
    @JoinColumn(name = "Dealer")
    private String dealer;
    /**
     * 退出原因
     */
    @Column(name = "Reason", length = 1000)
    private String reason;


}
