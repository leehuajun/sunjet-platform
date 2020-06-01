package com.sunjet.backend.controller.activity;

import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityNoticeView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.modules.asms.service.activity.ActivityNoticeService;
import com.sunjet.dto.asms.activity.ActivityNoticeItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 活动通知
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@RestController
@RequestMapping("/activityNotice")
public class ActivityNoticeController {

    @Autowired
    private ActivityNoticeService activityNoticeService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ActivityNoticeView> getPageList(@RequestBody PageParam<ActivityNoticeItem> pageParam) {
        return activityNoticeService.getPageList(pageParam);
    }

    @GetMapping("/findAll")
    public List<ActivityNoticeEntity> findAll() {
        return activityNoticeService.findAll();
    }

    @PostMapping("/findOneById")
    public ActivityNoticeEntity findOneById(@RequestBody String objId) {
        return activityNoticeService.findOneById(objId);
    }

    @PostMapping("/save")
    public ActivityNoticeEntity save(@RequestBody ActivityNoticeEntity activityNoticeEntity) {
        return activityNoticeService.save(activityNoticeEntity);
    }

    @DeleteMapping("/deleteById")
    public boolean deleteById(@RequestBody String objId) {
        return activityNoticeService.deleteById(objId);
    }

    /**
     * 搜索已关闭的活动通知
     *
     * @param map
     * @return
     */
    @PostMapping("/searchCloseActivityNotices")
    public List<ActivityNoticeEntity> searchCloseActivityNotices(@RequestBody Map<String, Object> map) {
        return activityNoticeService.searchCloseActivityNotices(map);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return activityNoticeService.startProcess(variables);
    }

    /**
     * 通过单据编号查询调拨供货单子行
     *
     * @param docNo
     * @return
     */
    @PostMapping("/findSupplyItemIdsByDocNo")
    public List<SupplyItemInfo> findSupplyItemIdsByDocNo(@RequestBody String docNo) {
        return activityNoticeService.findSupplyItemIdsByDocNo(docNo);
    }

    /**
     * 通过单据编号获取Id
     *
     * @param map
     * @return
     */
    @PostMapping("findAllobjIdByDocNo")
    public List<String> findAllobjIdByDocNo(@RequestBody Map<String, String> map) {
        return activityNoticeService.findAllobjIdByDocNo(map.get("activityNoticeDocNo"));
    }

    @PostMapping("findActivityVehicleItemsByNoticeId")
    public List<ActivityVehicleView> findActivityVehicleItemsByNoticeId(@RequestBody String noticeId) {
        return activityNoticeService.findActivityVehicleItemsByNoticeId(noticeId);
    }

    @PostMapping("findActivityPartItemsByNoticeId")
    public List<ActivityPartView> findActivityPartItemsByNoticeId(@RequestBody String noticeId) {
        return activityNoticeService.findActivityPartItemsByNoticeId(noticeId);
    }

    @PostMapping("findAllByObjIds")
    public List<ActivityNoticeEntity> findAllByObjIds(@RequestBody Set<String> objIds) {
        return activityNoticeService.findAllByObjIds(objIds);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return activityNoticeService.desertTask(objId);
    }

}
