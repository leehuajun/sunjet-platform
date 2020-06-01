package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 调拨供货通知单子行清单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeItemRepository extends JpaRepository<SupplyNoticeItemEntity, String>, JpaSpecificationExecutor<SupplyNoticeItemEntity> {

    /**
     * 通过调拨通知单id查一个调拨子行
     *
     * @param supplyNoticeId
     * @return
     */
    @Query("select p from SupplyNoticeItemEntity p where p.supplyNoticeId = ?1")
    List<SupplyNoticeItemEntity> findByNoticeId(String supplyNoticeId);


    /**
     * 删除通知行
     *
     * @param supplyNoticeId
     * @return
     */
    @Modifying
    @Query("delete from SupplyNoticeItemEntity p where p.supplyNoticeId = ?1")
    void deleteBySupplyNotices(String supplyNoticeId);

    @Query("select sni from SupplyNoticeItemEntity sni where sni.supplyNoticeId in(?1)")
    List<SupplyNoticeItemEntity> findAllBySupplyNoticeObjIds(List<String> supplyNoticeIds);

    @Query("select sni from SupplyNoticeItemEntity sni where sni.srcDocNo like ?1")
    List<SupplyNoticeItemEntity> findAllBySrcDocNo(String srcDocNo);

    /**
     * 通过来源单号查询调拨通知单明细
     *
     * @param srcDocNo
     * @return
     */
    @Query("select sni from SupplyNoticeItemEntity  sni where sni.srcDocID in (?1)")
    List<SupplyNoticeItemEntity> findAllBySrcDocIds(List<String> srcDocNo);

    @Query("select sni from SupplyNoticeItemEntity sni where sni.supplyNoticeId =?1")
    List<SupplyNoticeItemEntity> findAllBySupplyNoticeObjId(String objId);
}
