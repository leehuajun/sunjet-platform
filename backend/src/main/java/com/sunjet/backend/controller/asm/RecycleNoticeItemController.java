package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticePendingView;
import com.sunjet.backend.modules.asms.service.recycle.RecycleNoticeItemService;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/8/11.
 * 故障件返回通知列表
 */
@Slf4j
@RestController
@RequestMapping("/recycleNoticeItem")
public class RecycleNoticeItemController {

    @Autowired
    private RecycleNoticeItemService recycleNoticeItemService;

    @PostMapping("/findByNoticeId")
    public List<RecycleNoticeItemInfo> findByNoticeId(@RequestBody String objId) {
        return recycleNoticeItemService.findByNoticeId(objId);
    }

    @PostMapping("/save")
    public RecycleNoticeItemInfo save(@RequestBody RecycleNoticeItemInfo recycleNoticeItemInfo) {
        return recycleNoticeItemService.save(recycleNoticeItemInfo);
    }

    @PostMapping("/getPageList")
    public PageResult<RecycleNoticePendingView> getPageList(@RequestBody PageParam<RecycleNoticePendingItem> pageParam) {
        return recycleNoticeItemService.getPageList(pageParam);
    }

    /**
     * 根据配件编号或配件名称搜索配件需求列表
     *
     * @param map
     * @return
     */
    @PostMapping("/findByReturnOrParts")
    public List<RecycleNoticePendingView> findByReturnOrParts(@RequestBody Map<String, RecycleNoticePendingInfo> map) {
        RecycleNoticePendingInfo recycleNoticePendingItem = map.get("recycleNoticePendingItem");
        return recycleNoticeItemService.findByReturnOrParts(recycleNoticePendingItem.getPartCode(), recycleNoticePendingItem.getDealerCode());
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "删除故障件返回通知单明细")
    @DeleteMapping("delete")
    public Boolean delete(@RequestBody String objId) {
        return recycleNoticeItemService.delete(objId);
    }


    /**
     * 通过objid查找一个实体
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "通过objid查找实体")
    @PostMapping("findOneByObjid")
    public RecycleNoticeItemInfo findOneByObjid(@RequestBody String objId) {
        return recycleNoticeItemService.findOne(objId);
    }


    /**
     * 通过车辆vin码查询故障件返回通知单明细
     *
     * @param map
     * @return
     */
    @PostMapping("findAllRecycleItemsObjIdByVin")
    public List<String> findAllRecycleItemsObjIdByVin(@RequestBody Map<String, String> map) {
        return recycleNoticeItemService.findAllRecycleItemsObjIdByVin(map.get("vin"));
    }


    ///**
    // * 根据配件编号或名字关键字搜索故障件配件信息
    // * @param key
    // * @return
    // */
    //@PostMapping("/findCanReturnParts")
    //public List<RecycleNoticeItemInfo> findCanReturnParts(@RequestBody String key) {
    //    return recycleNoticeItemService.findCanReturnParts(key);
    //}
}
