package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.NoticeEntity;
import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface NoticeService {
    NoticeEntity save(NoticeEntity notice);

    boolean delete(String objId);

    NoticeEntity findOne(String objId);

    List<NoticeEntity> findAll();

    PageResult<NoticeEntity> getPageList(PageParam<NoticeEntity> pageParam);

    List<NoticeEntity> findLastNotices(int days);
}
