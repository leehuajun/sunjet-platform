package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 调拨供货子行
 */
public interface SupplyItemRepository extends JpaRepository<SupplyItemEntity, String>, JpaSpecificationExecutor<SupplyItemEntity> {


    /**
     * 根据供货单id查供货单子行
     *
     * @param supplyId
     * @return
     */
    @Query("select u from SupplyItemEntity u where u.supplyId = ?1 ")
    List<SupplyItemEntity> findBySupplyId(String supplyId);


    //@Query("select u from SupplyItemEntity u where u.supply.objId like ?1 ")
    //List<SupplyItemEntity> findSupplyItemsByDocID(String docid);

    @Query("select u from SupplyItemEntity u where u.supplyWaitingItemId = ?1 ")
    List<SupplyItemEntity> findBySupplyWaitingItemId(String waitingItemId);


    @Query("select si from SupplyItemEntity si where si.supplyNoticeItemId =?1")
    List<SupplyItemEntity> findBySupplyNoticeItemId(String noticeItemId);


    /**
     * 删除调拨供货行
     *
     * @param noticeItemId
     */
    @Modifying
    @Query("delete from SupplyItemEntity si where si.supplyNoticeItemId =?1")
    void deleteBySupplyNoticeItems(String noticeItemId);


    /**
     * 通过调拨供货单父表objId
     *
     * @param objId
     */
    @Modifying
    @Query("delete from SupplyItemEntity si where si.supplyId =?1")
    void deleteBySupplyObjId(String objId);

    @Query("select si from SupplyItemEntity si where si.supplyNoticeItemId in(?1)")
    List<SupplyItemEntity> findAllByNoticeItemId(List<String> objIds);

    @Query("select si from SupplyItemEntity si where si.supplyNoticeItemId =?1")
    List<SupplyItemEntity> findAllByNoticeItemId(String objId);
}
