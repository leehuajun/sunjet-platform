package com.sunjet.backend.modules.asms.repository.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 调拨供货 通知单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeRepository extends JpaRepository<SupplyNoticeEntity, String>, JpaSpecificationExecutor<SupplyNoticeEntity> {
    @Query("select p from SupplyNoticeEntity p where p.srcDocNo like ?1")
    SupplyNoticeEntity findOneBySrcDocNo(String srcDocNo);

    @Query("select sn from SupplyNoticeEntity sn where sn.srcDocID in (?1)")
    List<SupplyNoticeEntity> findAllBySrcobjIds(List<String> srcObjids);

    @Query("select p from SupplyNoticeEntity p where p.docNo like ?1")
    SupplyNoticeEntity findOneByDocNo(String supplyNotcieDocNo);

    @Query("select sn from SupplyNoticeEntity sn where sn.srcDocID = ?1 ")
    SupplyNoticeEntity findOneBySrcDocId(String objId);

    //@Query("select p from SupplyNoticeEntity p where p.docNo like ?1")
    //public List<SupplyNoticeEntity> getSupplyNoticeList(String docNo);
    //
    //@Query("select p from SupplyNoticeEntity p where p.createdTime >?1   order by p.createdTime desc")
    //public List<SupplyNoticeEntity> getLastDocNo(Date currentdate);
    //
    //@Query("select rp from SupplyNoticeEntity rp left join fetch rp.items where rp.objId=?1")
    //public SupplyNoticeEntity getSupplyNotice(String id);
}
