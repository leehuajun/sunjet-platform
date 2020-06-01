package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by zyh on 2016/11/21.
 * 待发货清单
 */
public interface SupplyWaitingItemService {
    //List<SupplyWaitingItemEntity> findSupplyWaitingItems(String supplyID);
    //
    //SupplyWaitingItemEntity findSupplyWaitingItemById(String objId);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<SupplyWaitingItemItem> getPageList(PageParam<SupplyWaitingItemItem> pageParam);


    public SupplyWaitingItemItem save(SupplyWaitingItemItem supplyWaitingItemItem);

    SupplyWaitingItemItem findSupplyWaitingItemById(String objId);

    Boolean delete(String objId);

    List<String> findAllObjIdsBySrcDocNo(String srcDocNo);

    List<String> findAllObjIdsByVin(String vin);

    List<String> findAllObjIdsBySupplyNoticeDocNo(String supplyNotcieDocNo);

    List<SupplyWaitingItemItem> findAllPartByAgency(String agencyCode, String dealerCode, String partName);

    List<SupplyWaitingItemItem> findAllByAgencyCode(String agencyCode);
}

