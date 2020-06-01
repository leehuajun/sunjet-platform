package com.sunjet.backend.controller.supply;

import com.sunjet.backend.modules.asms.entity.supply.SupplyItemEntity;
import com.sunjet.backend.modules.asms.service.supply.SupplyItemService;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/19.
 * 调拨供货子行
 */
@RestController
@RequestMapping("/supplyItem")
public class SupplyItemController {

    @Autowired
    SupplyItemService supplyItemService;

    /**
     * 通过objId删除
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "通过objId删除")
    @DeleteMapping("/deleteByObjId")
    public Boolean deleteByObjId(@RequestBody String objId) {
        return supplyItemService.delete(objId);
    }


    @ApiOperation(value = "通过调拨供货单父表objId删除")
    @DeleteMapping("/deleteBySupplyObjId")
    public Boolean deleteBySupplyObjId(@RequestBody String objId) {
        return supplyItemService.deleteBySupplyObjId(objId);
    }


    /**
     * 获取调拨供货明细
     *
     * @param objId 调拨通知子行objId
     * @return
     */
    @ApiOperation(value = "获取调拨供货明细")
    @PostMapping("/findAllByNoticeItemId")
    public List<SupplyItemInfo> findAllByNoticeItemId(@RequestBody String objId) {
        return supplyItemService.findAllByNoticeItemId(objId);
    }

    /**
     * 通过objId获取一条供货单
     *
     * @return
     */
    @PostMapping("/findOneByID")
    public SupplyItemEntity findOneByID(@RequestBody String objId) {
        return supplyItemService.findOne(objId);
    }

}
