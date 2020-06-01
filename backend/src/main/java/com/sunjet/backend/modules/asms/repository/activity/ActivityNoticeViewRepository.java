package com.sunjet.backend.modules.asms.repository.activity;


import com.sunjet.backend.modules.asms.entity.activity.view.ActivityNoticeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务活动通知单
 * Created by lhj on 16/9/17.
 */
public interface ActivityNoticeViewRepository extends JpaRepository<ActivityNoticeView, String>, JpaSpecificationExecutor<ActivityNoticeView> {

}
