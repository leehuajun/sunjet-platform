package com.sunjet.backend.modules.asms.repository.basic;

import com.sunjet.backend.modules.asms.entity.basic.view.DealerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by hhn on 2017/8/3.
 */
public interface DealerViewRepository extends JpaRepository<DealerView, String>, JpaSpecificationExecutor<DealerView> {
}
