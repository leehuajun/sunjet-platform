package com.sunjet.backend.controller.supply;

import com.sunjet.backend.modules.asms.service.supply.SupplyNoticeItemService;
import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调拨分配
 */
@RestController
@RequestMapping("/supplyAllocation")
public class SupplyAllocationController {

    @Autowired
    private SupplyNoticeItemService supplyNoticeItemService;


    /**
     * 新增
     *
     * @param supplyNoticeItemInfo
     * @return
     */
    @PostMapping("/save")
    public SupplyNoticeItemInfo save(@RequestBody SupplyNoticeItemInfo supplyNoticeItemInfo) {
        return supplyNoticeItemService.save(supplyNoticeItemInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return supplyNoticeItemService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<SupplyAllocationItem> getPageList(@RequestBody PageParam<SupplyAllocationItem> pageParam) {
        return supplyNoticeItemService.getPageList(pageParam);
    }

    @PostMapping("/findOne")
    public SupplyNoticeItemInfo findOne(@RequestBody String objId) {
        return supplyNoticeItemService.findOne(objId);
    }

    /**
     * 通过vin查询调拨通知明细objId
     *
     * @param map
     * @return 调拨通知单明细objIds
     */
    @RequestMapping("findAllObjIdByVin")
    public List<String> findAllObjIdByVin(@RequestBody Map<String, String> map) {
        return supplyNoticeItemService.findAllObjIdByVin(map.get("vin"));
    }
}
