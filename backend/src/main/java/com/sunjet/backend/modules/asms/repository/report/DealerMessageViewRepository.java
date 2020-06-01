package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.DealerMessageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 服务站信息视图
 */
public interface DealerMessageViewRepository extends JpaRepository<DealerMessageView, String>, JpaSpecificationExecutor<DealerMessageView> {

}
