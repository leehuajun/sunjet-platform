package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityVehicleViewRepository extends JpaRepository<ActivityVehicleView, String>, JpaSpecificationExecutor<ActivityVehicleView> {

    @Query("select avv from ActivityVehicleView avv where avv.activityNoticeId=?1")
    List<ActivityVehicleView> findActivityVehicleItemsByNoticeId(String noticeId);

    @Query("select avv from ActivityVehicleView avv where avv.activityDistributionId=?1")
    List<ActivityVehicleView> findActivityVehicleItemsDistributionId(String distributionId);
}
