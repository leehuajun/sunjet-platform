package com.sunjet.backend.controller.activity;

import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.backend.modules.asms.service.activity.ActivityPartService;
import com.sunjet.dto.asms.activity.ActivityPartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动通知
 * Created by zyf on 2017/8/10.
 */
@Slf4j
@RestController
@RequestMapping("/activityPart")
public class ActivityPartController {

    @Autowired
    private ActivityPartService activityPartService;

    @PostMapping("/findPartByActivityNoticeId")
    public List<ActivityPartView> findPartByActivityNoticeId(@RequestBody String activityNoticeId) {
        return activityPartService.findPartByActivityNoticeId(activityNoticeId);
    }

    @PostMapping("/findOneById")
    public ActivityPartInfo findOneById(@RequestBody String objId) {
        return activityPartService.findOneById(objId);
    }

    @PostMapping("/save")
    public ActivityPartInfo save(@RequestBody ActivityPartInfo activityPartInfo) {

        return activityPartService.save(activityPartInfo);
    }

    @DeleteMapping("/deleteById")
    public boolean deleteById(@RequestBody String objId) {
        return activityPartService.deleteById(objId);
    }

    /**
     * 通过活动通知单ID删除该活动单下面的所有活动配件
     *
     * @param activityNoticeId
     * @return
     */
    @DeleteMapping("/deleteByActivityNoticeId")
    public boolean deleteByActivityNoticeId(@RequestBody String activityNoticeId) {
        return activityPartService.deleteByActivityNoticeId(activityNoticeId);
    }

}
