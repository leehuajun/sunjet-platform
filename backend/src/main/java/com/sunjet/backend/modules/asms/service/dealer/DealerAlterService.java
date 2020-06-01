package com.sunjet.backend.modules.asms.service.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAlterRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.dealer.DealerAlterRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAlterRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface DealerAlterService extends BaseService {

    PageResult<DealerAlterRequestView> getPageList(PageParam<DealerAlterRequestItem> pageParam);

    boolean deleteByObjId(String objId);

    boolean delete(DealerAlterRequestInfo info);

    DealerAlterRequestInfo findOneById(String objId);

    DealerAlterRequestInfo save(DealerAlterRequestInfo dealerAlterRequestInfo);

    List<DealerAlterRequestInfo> findAll();

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
