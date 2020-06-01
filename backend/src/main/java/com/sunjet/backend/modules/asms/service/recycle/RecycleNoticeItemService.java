package com.sunjet.backend.modules.asms.service.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticePendingView;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface RecycleNoticeItemService {

    //List<RecycleNoticeItemEntity> findAllPart(String partCode);
    //
    //List<RecycleNoticeItemEntity> findCanReturnPart(String partCode, String dealerCode);

    RecycleNoticeItemInfo save(RecycleNoticeItemInfo recycleNoticeItemInfo);

    RecycleNoticeItemInfo findOne(String objId);

    boolean delete(RecycleNoticeItemInfo recycleNoticeItemInfo);

    boolean delete(String objId);

    List<RecycleNoticeItemInfo> findByNoticeId(String objId);

    boolean deleteByRecycleNoticeId(String objId);

    PageResult<RecycleNoticePendingView> getPageList(PageParam<RecycleNoticePendingItem> pageParam);

    //List<RecycleNoticeItemInfo> findCanReturnParts(String key);

    List<RecycleNoticePendingView> findByReturnOrParts(String key, String declerCode);

    List<String> findAllRecycleItemsObjIdByVin(String vin);
}
