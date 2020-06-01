package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyWaitingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 待返回清单
 * Created by Administrator on 2016/10/26.
 */
public interface SupplyWaitingItemRepository extends JpaRepository<SupplyWaitingItemEntity, String>, JpaSpecificationExecutor<SupplyWaitingItemEntity> {

    @Query("select sw from SupplyWaitingItemEntity sw where sw.supplyNoticeItemId in (?1)")
    List<SupplyWaitingItemEntity> findAllBySupplyNoticeObjIds(List<String> supplyNoticeObjIds);

    @Query("select sw from SupplyWaitingItemEntity sw where sw.agencyCode=?1 and sw.dealerCode=?2 and (sw.partCode like ?3 or sw.partName like ?3) and sw.surplusAmount>=1")
    List<SupplyWaitingItemEntity> findAllPartByAgency(String agencyCode, String dealerCode, String partName);

    @Query("select sw from SupplyWaitingItemEntity sw where sw.agencyCode=?1 and sw.surplusAmount>=1")
    List<SupplyWaitingItemEntity> findAllByAgencyCode(String agencyCode);


    //@Query("select u from SupplyWaitingItemEntity u where u.supplyNoticeItem.objId like ?1 ")
    //List<SupplyWaitingItemEntity> findSupplyWaitingItems(String supplyID);
    //
    //@Query("select u from SupplyWaitingItemEntity u where u.objId like ?1 ")
    //SupplyWaitingItemEntity findSupplyWaitingItemById(String objId);
}
