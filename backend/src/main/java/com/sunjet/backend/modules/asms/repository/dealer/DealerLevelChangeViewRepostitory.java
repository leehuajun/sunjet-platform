package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.view.DealerLevelChangeRequestView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zyf on 2017/8/4.
 * 服务站等级变更申请
 */
public interface DealerLevelChangeViewRepostitory extends JpaRepository<DealerLevelChangeRequestView, String>, JpaSpecificationExecutor<DealerLevelChangeRequestView> {

}
