package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.dto.asms.activity.ActivityPartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务活动配件子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityPartViewRepository extends JpaRepository<ActivityPartView, String>, JpaSpecificationExecutor<ActivityPartView> {

    @Query("select apv from ActivityPartView apv where apv.activityNoticeId = ?1 ")
    List<ActivityPartView> findActivityPartItemsByNoticeId(String activityNoticeId);
}
