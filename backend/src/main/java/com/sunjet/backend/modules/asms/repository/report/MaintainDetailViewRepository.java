package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.MaintainDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 服务明细视图
 */
public interface MaintainDetailViewRepository extends JpaRepository<MaintainDetailView, String>, JpaSpecificationExecutor<MaintainDetailView> {

}
