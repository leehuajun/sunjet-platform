package com.sunjet.backend.modules.asms.repository.asm;

import com.sunjet.backend.modules.asms.entity.asm.view.ActivityMaintenanceCommissionPartView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zyf on 2017/10/26.
 * 活动服务单视图
 */
public interface ActivityMaintenanceCommissionPartViewRepository extends JpaRepository<ActivityMaintenanceCommissionPartView, String>, JpaSpecificationExecutor<ActivityMaintenanceCommissionPartView> {

    @Query("select distinct  amcpv from ActivityMaintenanceCommissionPartView amcpv where amcpv.status <> -3 and amcpv.activityMaintenanceId in (?1) order by amcpv.createdTime desc")
    List<ActivityMaintenanceCommissionPartView> findCommissionPartInfoByActivityMaintenanceIdList(List<String> activityMaintenanceIdList);
}
