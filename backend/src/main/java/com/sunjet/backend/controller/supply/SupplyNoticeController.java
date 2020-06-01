package com.sunjet.backend.controller.supply;

import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import com.sunjet.backend.modules.asms.service.supply.SupplyNoticeService;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调拨通知单
 */
@RestController
@RequestMapping("/supplyNotice")
public class SupplyNoticeController {

    @Autowired
    private SupplyNoticeService supplyNoticeService;


    /**
     * 新增
     *
     * @param supplyNoticeInfo
     * @return
     */
    @PostMapping("/save")
    public SupplyNoticeInfo save(@RequestBody SupplyNoticeInfo supplyNoticeInfo) {
        return supplyNoticeService.save(supplyNoticeInfo);
    }

    /**
     * 删除
     *
     * @param ObjId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String ObjId) {
        return supplyNoticeService.delete(ObjId);
    }

    /**
     * 分页
     *
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<SupplyNoticeView> getPageList(@RequestBody PageParam<SupplyNoticeItem> pageParam) {
        return supplyNoticeService.getPageList(pageParam);
    }

    /**
     * 通过id查找一个实体对象
     *
     * @param ObjId
     * @return
     */
    @PostMapping("/findByOne")
    public SupplyNoticeInfo findByOne(@RequestBody String ObjId) {
        return supplyNoticeService.findOne(ObjId);
    }


    /**
     * 通过调拨通知单id查一个调拨子行
     *
     * @param supplyNoticeId
     * @return
     */
    @PostMapping("/findByNoticeId")
    public List<SupplyNoticeItemInfo> findByNoticeId(@RequestBody String supplyNoticeId) {
        return supplyNoticeService.findByNoticeId(supplyNoticeId);
    }

    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return supplyNoticeService.startProcess(variables);
    }

    /**
     * 检查收货状态
     *
     * @return
     */
    @ApiOperation(value = "检查收货状态")
    @PostMapping("checkSupplyReceiveState")
    public Boolean checkSupplyReceiveState(@RequestBody String objId) {
        return supplyNoticeService.checkSupplyReceiveState(objId);
    }


    /**
     * 通过来源单号查一个调拨子行
     *
     * @param map
     * @return
     */
    @PostMapping("/findSupplyItemIdsBySrcDocNo")
    public List<SupplyItemInfo> findSupplyItemIdsBySrcDocNo(@RequestBody Map<String, String> map) {
        return supplyNoticeService.findSupplyItemIdsBySrcDocNo(map.get("docNo"));
    }

    /**
     * 通过VIN查一个调拨子行
     *
     * @param map
     * @return
     */
    @PostMapping("/findSupplyItemIdsByVin")
    public List<SupplyItemInfo> findSupplyItemIdsByVin(@RequestBody Map<String, String> map) {
        return supplyNoticeService.findSupplyItemIdsByVin(map.get("vin"));
    }

    /**
     * 通过活动通知单查询调拨通知单objId集合
     *
     * @param map
     * @return
     */
    @PostMapping("findAllObjIdByActivityNoticeDocNo")
    public List<String> findAllObjIdByActivityNoticeDocNo(@RequestBody Map<String, String> map) {
        return supplyNoticeService.findAllObjIdByActivityNoticeDocNo(map.get("activityNoticeDocNo"));
    }


    /**
     * 通过VIN查询所有调拨通知单objid
     *
     * @param map
     * @return
     */
    @PostMapping("findSupplyNoticeIdsByVin")
    public List<String> findSupplyNoticeIdsByVin(@RequestBody Map<String, String> map) {
        return supplyNoticeService.findSupplyNoticeIdsByVin(map.get("vin"));
    }

    /**
     * 通过单号查询调拨通知单
     *
     * @param map 调拨通知单单号
     * @return
     */
    @PostMapping("findOneByDocNo")
    public SupplyNoticeInfo findOneByDocNo(@RequestBody Map<String, String> map) {
        return supplyNoticeService.findOneByDocNo(map.get("supplyNoticeDocNo"));
    }


    /**
     * 通过单号查询调拨通知单
     *
     * @param objId 调拨通知单单号
     * @return
     */
    @PostMapping("findOneBySrcDocId")
    public SupplyNoticeEntity findOneBySrcDocId(@RequestBody String objId) {
        return supplyNoticeService.findOneBySrcDocId(objId);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return supplyNoticeService.desertTask(objId);
    }

}
