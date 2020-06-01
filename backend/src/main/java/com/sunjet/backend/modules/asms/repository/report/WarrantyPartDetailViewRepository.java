package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.WarrantyPartDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/21.
 * 三包配件列表视图
 */
public interface WarrantyPartDetailViewRepository extends JpaRepository<WarrantyPartDetailView, String>, JpaSpecificationExecutor<WarrantyPartDetailView> {


}
