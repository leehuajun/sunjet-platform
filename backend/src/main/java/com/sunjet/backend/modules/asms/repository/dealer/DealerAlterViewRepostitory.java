package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAlterRequestView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zyf on 2017/8/4.
 * 服务站变更申请
 */
public interface DealerAlterViewRepostitory extends JpaRepository<DealerAlterRequestView, String>, JpaSpecificationExecutor<DealerAlterRequestView> {

}
