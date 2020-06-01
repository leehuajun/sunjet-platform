package com.sunjet.backend.modules.asms.service.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.RecycleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.recycle.RecycleItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 故障件返回
 * Created by lhj on 16/9/17.
 */
public interface RecycleService extends BaseService {
    //void save(RecycleEntity recycleRequest);
    //
    //RecycleEntity findRecycleListById(String objId);
    //
    //boolean deleteEntity(RecycleEntity entity);
    //
    //RecycleEntity findOneWithItems(String objId);

    RecycleInfo save(RecycleInfo recycleInfo);

    RecycleEntity findOne(String objId);

    boolean delete(RecycleInfo recycleInfo);

    boolean delete(String objId);

    PageResult<RecycleView> getPageList(PageParam<RecycleItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    List<String> findAllRecycleObjIdsBySrcDocNo(String srcDocNo);

    List<String> findAllRecycleObjIdsByVin(String vin);

    Boolean desertTask(String objId);
}
