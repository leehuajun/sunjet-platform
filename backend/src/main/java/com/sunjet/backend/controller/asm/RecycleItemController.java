package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleItemPartView;
import com.sunjet.backend.modules.asms.service.recycle.RecycleItemService;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/11.
 * 故障件返回单子行列表
 */
@Slf4j
@RestController
@RequestMapping("/recycleItem")
public class RecycleItemController {

    @Autowired
    private RecycleItemService recycleItemService;

    @PostMapping("/findByRecycle")
    public List<RecycleItemEntity> findByRecycle(@RequestBody String objId) {
        return recycleItemService.findByRecycle(objId);
    }

    @PostMapping("/findAllByNoticeItemId")
    public List<RecycleItemInfo> findAllByNoticeItemId(@RequestBody String objId) {
        return recycleItemService.findAllByNoticeItemId(objId);
    }

    @PostMapping("/save")
    public RecycleItemInfo save(@RequestBody RecycleItemInfo recycleItemInfo) {
        return recycleItemService.save(recycleItemInfo);
    }

    @PostMapping("/findByRecyclePartList")
    public List<RecycleItemPartView> findByRecyclePartList(@RequestBody String recycle) {
        return recycleItemService.findByRecyclePartList(recycle);
    }

    /**
     * 查询一个故障件返回配件
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneById")
    public RecycleItemEntity findOneById(@RequestBody String objId) {
        return recycleItemService.findOne(objId);
    }

    /**
     * 故障件子行删除
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "故障件子行删除")
    @DeleteMapping("delete")
    public Boolean delete(@RequestBody String objId) {
        return recycleItemService.delete(objId);
    }
}
