package com.sunjet.backend.modules.asms.service.agency;


import com.sunjet.backend.modules.asms.entity.agency.view.AgencyQuitRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.agency.AgencyQuitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyQuitRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyQuitService extends BaseService {
    // void save(AgencyQuitRequestEntity entity);
    AgencyQuitRequestInfo save(AgencyQuitRequestInfo agencyQuitRequestInfo);

    boolean delete(AgencyQuitRequestInfo agencyQuitRequestInfo);

    boolean delete(String objId);

    AgencyQuitRequestInfo findOne(String objId);

    PageResult<AgencyQuitRequestView> getPageList(PageParam<AgencyQuitRequestItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);

    //AgencyQuitRequestEntity findOneById(String businessId);
    //void deleteEntity(AgencyQuitRequestEntity entity);
}
