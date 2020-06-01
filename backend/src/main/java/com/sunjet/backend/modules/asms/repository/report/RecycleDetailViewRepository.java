package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.RecycleDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 故障件明细视图
 */
public interface RecycleDetailViewRepository extends JpaRepository<RecycleDetailView, String>, JpaSpecificationExecutor<RecycleDetailView> {

}
