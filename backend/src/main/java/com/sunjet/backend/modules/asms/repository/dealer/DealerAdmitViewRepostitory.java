package com.sunjet.backend.modules.asms.repository.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAdmitRequestView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务站准入申请View
 * Created by zyf on 2017/8/3.
 */
public interface DealerAdmitViewRepostitory extends JpaRepository<DealerAdmitRequestView, String>, JpaSpecificationExecutor<DealerAdmitRequestView> {
}
