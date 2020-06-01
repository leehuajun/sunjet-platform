package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 * 调拨通知
 */
public interface SupplyNoticeItemService {


    //SupplyNoticeItemEntity findOne(String objId);

    SupplyNoticeItemInfo save(SupplyNoticeItemInfo supplyNoticeItemInfo);

    SupplyNoticeItemInfo findOne(String objId);

    boolean delete(SupplyNoticeItemInfo supplyNoticeItemInfo);

    boolean delete(String objId);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<SupplyAllocationItem> getPageList(PageParam<SupplyAllocationItem> pageParam);

    List<String> findAllObjIdByVin(String vin);
}
