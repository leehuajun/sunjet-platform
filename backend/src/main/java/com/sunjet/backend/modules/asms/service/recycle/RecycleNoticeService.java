package com.sunjet.backend.modules.asms.service.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticeView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 故障件返回通知
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticeService extends BaseService {
    //void save(RecycleNoticeEntity recycleNoticeRequest);
    //
    //RecycleNoticeEntity findOneWithOthersId(String objId);
    //
    //void deleteEntity(RecycleNoticeEntity entity);

    PageResult<RecycleNoticeView> getPageList(PageParam<RecycleNoticeItem> pageParam);

    RecycleNoticeInfo save(RecycleNoticeInfo recycleNoticeInfo);

    RecycleNoticeInfo findOne(String objId);

    boolean delete(RecycleNoticeInfo recycleNoticeInfo);

    boolean delete(String objId);


    Map<String, String> startProcess(Map<String, Object> variables);

    List<String> findAllRecycleNoticeObjIdsByVin(String vin);

    Boolean desertTask(String objId);
}
