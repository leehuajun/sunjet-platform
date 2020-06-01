package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyDisItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 二次分配
 */
public interface SupplyDisItemRepository extends JpaRepository<SupplyDisItemEntity, String>, JpaSpecificationExecutor<SupplyDisItemEntity> {
    @Query("select sd from SupplyDisItemEntity sd where sd.supplyNoticeItemId in (?1)")
    List<SupplyDisItemEntity> finDAllBySupplyNotice(List<String> supplyNoticeItemObjId);
}
