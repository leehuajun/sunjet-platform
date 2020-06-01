package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.dto.asms.activity.ActivityPartInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityPartService {

    boolean deleteById(String objId);

    ActivityPartInfo findOneById(String objId);

    ActivityPartInfo save(ActivityPartInfo activityPartInfo);

    List<ActivityPartView> findPartByActivityNoticeId(String activityNoticeId);

    boolean deleteByActivityNoticeId(String activityNoticeId);
}
