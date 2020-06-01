package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticeView;
import com.sunjet.backend.modules.asms.service.recycle.RecycleNoticeService;
import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/8/10.
 * 故障件返回通知
 */
@Slf4j
@RestController
@RequestMapping("/recycleNotice")
public class RecycleNoticeController {

    @Autowired
    private RecycleNoticeService recycleNoticeService;

    @PostMapping("/getPageList")
    public PageResult<RecycleNoticeView> getPageList(@RequestBody PageParam<RecycleNoticeItem> pageParam) {
        return recycleNoticeService.getPageList(pageParam);
    }

    @PostMapping("/findOneById")
    public RecycleNoticeInfo findOneById(@RequestBody String objId) {
        return recycleNoticeService.findOne(objId);
    }

    @ApiOperation(value = "保存故障件返回单")
    @PostMapping("/save")
    public RecycleNoticeInfo save(@RequestBody RecycleNoticeInfo recycleNoticeInfo) {
        return recycleNoticeService.save(recycleNoticeInfo);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return recycleNoticeService.delete(objId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return recycleNoticeService.startProcess(variables);
    }

    /**
     * 通过车辆vin码查询故障件返回单
     *
     * @param map
     * @return
     */
    @PostMapping("findAllRecycleNoticeObjIdsByVin")
    public List<String> findAllRecycleNoticeObjIdsByVin(@RequestBody Map<String, String> map) {
        return recycleNoticeService.findAllRecycleNoticeObjIdsByVin(map.get("vin"));
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return recycleNoticeService.desertTask(objId);
    }

}
