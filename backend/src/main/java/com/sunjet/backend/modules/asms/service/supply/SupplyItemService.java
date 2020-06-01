package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.backend.modules.asms.entity.supply.SupplyItemEntity;
import com.sunjet.dto.asms.supply.SupplyItemInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 调拨供货子行
 */
public interface SupplyItemService {
    //List<SupplyItemEntity> findSupplyItemsByDocID(String docid);

    SupplyItemInfo save(SupplyItemInfo supplyItemInfo);

    SupplyItemEntity findOne(String objId);

    boolean delete(SupplyItemInfo supplyItemInfo);

    boolean delete(String objId);

    List<SupplyItemInfo> findBySupplyId(String supplyId);

    boolean deleteBySupplyNoticeItems(String noticeItemId);

    Boolean deleteBySupplyObjId(String objId);

    List<SupplyItemInfo> findAllByNoticeItemId(String objId);

    List<SupplyItemInfo> findAllByNoticeItemId(List<String> objIds);


}
