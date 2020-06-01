package com.sunjet.backend.modules.asms.service.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.view.DealerLevelChangeRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestInfo;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 服务站等级变更申请
 * Created by Administrator on 2016/9/2.
 */
public interface DealerLevelChangeService extends BaseService {

    List<DealerLevelChangeRequestInfo> findAll();

    DealerLevelChangeRequestInfo save(DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo);

    DealerLevelChangeRequestInfo findOneById(String objId);

    boolean delete(DealerLevelChangeRequestInfo info);

    boolean deleteByObjId(String objId);

    PageResult<DealerLevelChangeRequestView> getPageList(PageParam<DealerLevelChangeRequestItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
