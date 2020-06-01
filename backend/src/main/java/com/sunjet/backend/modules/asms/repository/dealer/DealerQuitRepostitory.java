package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.DealerQuitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 * 服务站退出申请DAO
 */
public interface DealerQuitRepostitory extends JpaRepository<DealerQuitRequestEntity, String>, JpaSpecificationExecutor<DealerQuitRequestEntity> {

}
