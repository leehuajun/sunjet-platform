package com.sunjet.backend.modules.asms.repository.asm;

import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/3.
 * 三包服务单视图
 */
public interface WarrantyMaintenanceViewRepository extends JpaRepository<WarrantyMaintenanceView, String>, JpaSpecificationExecutor<WarrantyMaintenanceView> {
}
