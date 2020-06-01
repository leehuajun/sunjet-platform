package com.sunjet.backend.controller.asm;

import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleView;
import com.sunjet.backend.modules.asms.service.recycle.RecycleService;
import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.recycle.RecycleItem;
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
 * 故障件返回单
 */
@Slf4j
@RestController
@RequestMapping("/recycle")
public class RecycleController {

    @Autowired
    private RecycleService recycleService;

    @PostMapping("/getPageList")
    public PageResult<RecycleView> getPageList(@RequestBody PageParam<RecycleItem> pageParam) {
        return recycleService.getPageList(pageParam);
    }

    @PostMapping("/findOneById")
    public RecycleEntity findOneById(@RequestBody String objId) {
        return recycleService.findOne(objId);
    }

    /**
     * 保存故障件返回通知单
     *
     * @param recycleInfo
     * @return
     */
    @ApiOperation(value = "故障件返回通知单")
    @PostMapping("/save")
    public RecycleInfo save(@RequestBody RecycleInfo recycleInfo) {
        return recycleService.save(recycleInfo);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return recycleService.delete(objId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return recycleService.startProcess(variables);
    }


    /**
     * 通过来源单号查询故障件objId集合
     *
     * @param map
     * @return
     */
    @PostMapping("findAllRecycleObjIdsBySrcDocNo")
    public List<String> findAllRecycleObjIdsBySrcDocNo(@RequestBody Map<String, String> map) {
        return recycleService.findAllRecycleObjIdsBySrcDocNo(map.get("srcDocNo"));
    }

    /**
     * 通过vin 查询故障件objId集合
     *
     * @param map
     * @return
     */
    @PostMapping("findAllRecycleObjIdsByVin")
    public List<String> findAllRecycleObjIdsByVin(@RequestBody Map<String, String> map) {
        return recycleService.findAllRecycleObjIdsByVin(map.get("vin"));
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return recycleService.desertTask(objId);
    }

}
