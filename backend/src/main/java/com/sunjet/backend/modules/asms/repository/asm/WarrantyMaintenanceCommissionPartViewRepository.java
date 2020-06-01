package com.sunjet.backend.modules.asms.repository.asm;

import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceCommissionPartView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zyf on 2017/10/26.
 * 三包服务单视图
 */
public interface WarrantyMaintenanceCommissionPartViewRepository extends JpaRepository<WarrantyMaintenanceCommissionPartView, String>, JpaSpecificationExecutor<WarrantyMaintenanceCommissionPartView> {

    @Query("select distinct  mhv from WarrantyMaintenanceCommissionPartView mhv where  mhv.status <> -3 and mhv.warrantyMaintenance in (?1) order by mhv.createdTime desc")
    List<WarrantyMaintenanceCommissionPartView> findCommissionPartInfoByWarrantyMaintenanceIdList(List<String> warrantyMaintenanceIdList);
}
