package com.sunjet.backend.modules.asms.repository.activity;

import com.sunjet.backend.modules.asms.entity.activity.view.ActivityMaintenanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zyf on 2017/8/28.
 * 活动服务view
 */
public interface ActivityMaintenanceViewRepository extends JpaRepository<ActivityMaintenanceView, String>, JpaSpecificationExecutor<ActivityMaintenanceView> {
}
