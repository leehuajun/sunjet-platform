package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.RecycleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/11/6.
 * 故障件标签
 */
public interface RecycleLabelViewRepository extends JpaRepository<RecycleLabel, String>, JpaSpecificationExecutor<RecycleLabel> {
}
