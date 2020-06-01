package com.sunjet.backend.modules.asms.service.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleItemPartView;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface RecycleItemService {

    RecycleItemInfo save(RecycleItemInfo recycleItemInfo);

    RecycleItemEntity findOne(String objId);

    boolean delete(RecycleItemInfo recycleItemInfo);

    boolean delete(String objId);

    List<RecycleItemEntity> findByRecycle(String objId);

    List<RecycleItemInfo> findAllByNoticeItemId(String objId);

    List<RecycleItemPartView> findByRecyclePartList(String recycle);

    boolean deleteByRecycleObjId(String objId);

    List<RecycleItemEntity> findAllRecycleItemBySrcDocNo(String srcDocNo);

    List<RecycleItemEntity> findAllRecycleItemBySrcDocNos(List<String> srcDocIds);
}
