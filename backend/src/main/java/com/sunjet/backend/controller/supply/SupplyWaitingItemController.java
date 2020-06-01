package com.sunjet.backend.controller.supply;

import com.sunjet.backend.modules.asms.service.supply.SupplyWaitingItemService;
import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 待发货清单
 */
@RestController
@RequestMapping("/supplyWaitingItem")
public class SupplyWaitingItemController {

    @Autowired
    private SupplyWaitingItemService supplyWaitingItemService;

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<SupplyWaitingItemItem> getPageList(@RequestBody PageParam<SupplyWaitingItemItem> pageParam) {
        return supplyWaitingItemService.getPageList(pageParam);
    }

    /**
     * 保存待发货清单
     *
     * @param supplyWaitingItemItem
     * @return
     */
    @PostMapping("/save")
    public SupplyWaitingItemItem save(@RequestBody SupplyWaitingItemItem supplyWaitingItemItem) {
        return supplyWaitingItemService.save(supplyWaitingItemItem);
    }

    /**
     * 通过objId查找实体
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "通过objId查找待发货")
    @PostMapping("findSupplyWaitingItemById")
    public SupplyWaitingItemItem findSupplyWaitingItemById(@RequestBody String objId) {
        return supplyWaitingItemService.findSupplyWaitingItemById(objId);
    }


    /**
     * 通过objId删除
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "删除待返回清单")
    @DeleteMapping("delete")
    public Boolean delete(@RequestBody String objId) {
        return supplyWaitingItemService.delete(objId);
    }


    /**
     * 通过SrcDocNo查询所有调拨待发货单objid
     *
     * @param map
     * @return
     */
    @PostMapping("findAllObjIdsBySrcDocNo")
    public List<String> findAllObjIdsBySrcDocNo(@RequestBody Map<String, String> map) {
        return supplyWaitingItemService.findAllObjIdsBySrcDocNo(map.get("srcDocNo"));
    }

    /**
     * 通过vin查询所有调拨待发货单objid
     *
     * @param map
     * @return
     */
    @PostMapping("findAllObjIdsByVin")
    public List<String> findAllObjIdsByVin(@RequestBody Map<String, String> map) {
        return supplyWaitingItemService.findAllObjIdsByVin(map.get("vin"));
    }

    /**
     * 通过vin查询所有调拨待发货单objid
     *
     * @param map
     * @return
     */
    @PostMapping("findAllObjIdsBySupplyNoticeDocNo")
    public List<String> findAllObjIdsBySupplyNotcieDocNo(@RequestBody Map<String, String> map) {
        return supplyWaitingItemService.findAllObjIdsBySupplyNoticeDocNo(map.get("supplyNotcieDocNo"));
    }


    /**
     * 获取合作商待发货配件
     *
     * @param map
     * @return
     */
    @PostMapping("findAllPartByAgency")
    public List<SupplyWaitingItemItem> findAllPartByAgency(@RequestBody Map<String, String> map) {
        return supplyWaitingItemService.findAllPartByAgency(map.get("agencyCode"), map.get("dealerCode"), map.get("partName"));
    }


    /**
     * 创建调拨供货前获取可创建配件
     *
     * @param map
     * @return
     */
    @PostMapping("findAllByAgencyCode")
    public List<SupplyWaitingItemItem> findAllByAgencyCode(@RequestBody Map<String, String> map) {
        return supplyWaitingItemService.findAllByAgencyCode(map.get("agencyCode"));
    }

}
