package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.dto.asms.supply.SupplyDisItemInfo;
import com.sunjet.dto.asms.supply.SupplyDisItemItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by zyh on 2016/11/21.
 * 调拨分配
 */
public interface SupplyDisItemService {

    SupplyDisItemInfo save(SupplyDisItemInfo supplyDisItemInfo);

    SupplyDisItemInfo findOne(String objId);

    boolean delete(SupplyDisItemInfo supplyDisItemInfo);

    boolean delete(String objId);

    /**
     * 分页
     *
     * @return
     */
    public PageResult<SupplyDisItemItem> getPageList(PageParam<SupplyDisItemItem> pageParam);

    List<String> finAllObjIdBySrcDocNo(String srcDocNo);

    List<String> finAllObjIdByVin(String vin);

    List<String> finAllObjIdBySupplyNoticeDocNo(String supplyNotcieDocNo);
}
