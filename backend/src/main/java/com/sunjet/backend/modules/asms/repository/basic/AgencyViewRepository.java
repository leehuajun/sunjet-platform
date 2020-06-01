package com.sunjet.backend.modules.asms.repository.basic;

import com.sunjet.backend.modules.asms.entity.basic.view.AgencyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 合作商视图
 */
public interface AgencyViewRepository extends JpaRepository<AgencyView, String>, JpaSpecificationExecutor<AgencyView> {
}
