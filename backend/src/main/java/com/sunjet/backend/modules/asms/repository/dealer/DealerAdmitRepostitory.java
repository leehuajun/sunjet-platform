package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2016/9/5.
 * 服务站准入申请
 */
public interface DealerAdmitRepostitory extends JpaRepository<DealerAdmitRequestEntity, String>, JpaSpecificationExecutor<DealerAdmitRequestEntity> {

    @Query("select da from DealerAdmitRequestEntity da where da.processInstanceId=?1")
    DealerAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId);
}
