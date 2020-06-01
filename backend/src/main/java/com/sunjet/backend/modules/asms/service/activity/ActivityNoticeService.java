package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityNoticeView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.activity.ActivityNoticeItem;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 服务活动通知单
 * Created by lhj on 16/9/17.
 */
public interface ActivityNoticeService extends BaseService {

    ActivityNoticeEntity save(ActivityNoticeEntity activityNoticeEntity);

    List<ActivityNoticeEntity> findAll();

    ActivityNoticeEntity findOneById(String objId);

    PageResult<ActivityNoticeView> getPageList(PageParam<ActivityNoticeItem> pageParam);

    boolean deleteById(String objId);

    List<ActivityNoticeEntity> searchCloseActivityNotices(Map<String, Object> map);

    Map<String, String> startProcess(Map<String, Object> variables);

    List<SupplyItemInfo> findSupplyItemIdsByDocNo(String docNo);

    List<String> findAllobjIdByDocNo(String activityNoticeDocNo);

    List<ActivityVehicleView> findActivityVehicleItemsByNoticeId(String noticeId);

    List<ActivityPartView> findActivityPartItemsByNoticeId(String noticeId);

    List<ActivityNoticeEntity> findAllByObjIds(Set<String> objIds);

    Boolean desertTask(String objId);
}
