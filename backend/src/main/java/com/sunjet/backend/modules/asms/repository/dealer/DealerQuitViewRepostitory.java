package com.sunjet.backend.modules.asms.repository.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.view.DealerQuitRequestView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zyf on 2017/8/4.
 * 服务站退出申请
 */
public interface DealerQuitViewRepostitory extends JpaRepository<DealerQuitRequestView, String>, JpaSpecificationExecutor<DealerQuitRequestView> {

}
