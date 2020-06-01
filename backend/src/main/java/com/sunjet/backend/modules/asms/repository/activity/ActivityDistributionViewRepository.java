package com.sunjet.backend.modules.asms.repository.activity;

import com.sunjet.backend.modules.asms.entity.activity.view.ActivityDistributionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zyf on 2017/8/28.
 */
public interface ActivityDistributionViewRepository extends JpaRepository<ActivityDistributionView, String>, JpaSpecificationExecutor<ActivityDistributionView> {
}
