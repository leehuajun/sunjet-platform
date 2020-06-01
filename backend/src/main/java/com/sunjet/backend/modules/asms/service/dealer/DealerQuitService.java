package com.sunjet.backend.modules.asms.service.dealer;


import com.sunjet.backend.modules.asms.entity.dealer.view.DealerQuitRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.dealer.DealerQuitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface DealerQuitService extends BaseService {

    List<DealerQuitRequestInfo> findAll();

    DealerQuitRequestInfo save(DealerQuitRequestInfo dealerQuitRequestInfo);

    DealerQuitRequestInfo findOneById(String objId);

    boolean delete(DealerQuitRequestInfo info);

    boolean deleteByObjId(String objId);

    PageResult<DealerQuitRequestView> getPageList(PageParam<DealerQuitRequestItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
