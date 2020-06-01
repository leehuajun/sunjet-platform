package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * 调拨供货 通知单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeViewRepository extends JpaRepository<SupplyNoticeView, String>, JpaSpecificationExecutor<SupplyNoticeView> {

    @Query("select p from SupplyNoticeView p where p.objId in (?1)")
    public List<SupplyNoticeView> getSupplyNoticeListByIds(ArrayList<String> ids);

}
