package com.sunjet.backend.controller.supply;

import com.sunjet.backend.modules.asms.service.supply.SupplyDisItemService;
import com.sunjet.dto.asms.supply.SupplyDisItemInfo;
import com.sunjet.dto.asms.supply.SupplyDisItemItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 二次分配
 */
@RestController
@RequestMapping("/supplyDisItem")
public class SupplyDisItemController {
    @Autowired
    private SupplyDisItemService supplyDisItemService;


    /**
     * 新增
     *
     * @param supplyDisItemInfo
     * @return
     */
    @PostMapping("/save")
    public SupplyDisItemInfo save(@RequestBody SupplyDisItemInfo supplyDisItemInfo) {
        return supplyDisItemService.save(supplyDisItemInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return supplyDisItemService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<SupplyDisItemItem> getPageList(@RequestBody PageParam<SupplyDisItemItem> pageParam) {
        return supplyDisItemService.getPageList(pageParam);
    }

    /**
     * 查找二次分配
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "查找二次分配")
    @PostMapping("findOne")
    public SupplyDisItemInfo findOne(@RequestBody String objId) {
        return supplyDisItemService.findOne(objId);
    }


    /**
     * 通过SrcDocNo查询所有二次分配objid
     *
     * @param map
     * @return
     */
    @PostMapping("finAllObjIdBySrcDocNo")
    public List<String> finAllObjIdBySrcDocNo(@RequestBody Map<String, String> map) {
        return supplyDisItemService.finAllObjIdBySrcDocNo(map.get("srcDocNo"));
    }

    /**
     * 通过vin查询所有二次分配objid
     *
     * @param map
     * @return
     */
    @PostMapping("finAllObjIdByVin")
    public List<String> finAllObjIdByVin(@RequestBody Map<String, String> map) {
        return supplyDisItemService.finAllObjIdByVin(map.get("vin"));
    }

    /**
     * 通过vin查询所有二次分配objid
     *
     * @param map
     * @return
     */
    @PostMapping("finAllObjIdBySupplyNoticeDocNo")
    public List<String> finAllObjIdBySupplyNoticeDocNo(@RequestBody Map<String, String> map) {
        return supplyDisItemService.finAllObjIdBySupplyNoticeDocNo(map.get("supplyNotcieDocNo"));
    }

}
