package com.sunjet.backend.modules.asms.repository.report;

import com.sunjet.backend.modules.asms.entity.report.WarrantyPartsSelfPurchaseDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by SUNJET_WS on 2017/8/17.
 * 三包配件自购明细
 */
public interface WarrantyPartsSelfPurchaseDetailViewRepository extends JpaRepository<WarrantyPartsSelfPurchaseDetailView, String>, JpaSpecificationExecutor<WarrantyPartsSelfPurchaseDetailView> {

}
