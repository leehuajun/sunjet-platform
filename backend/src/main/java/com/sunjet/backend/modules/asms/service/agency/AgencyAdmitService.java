package com.sunjet.backend.modules.asms.service.agency;


import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAdmitRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface AgencyAdmitService extends BaseService {

    AgencyAdmitRequestInfo save(AgencyAdmitRequestInfo agencyAdmitRequestInfo);

    boolean delete(AgencyAdmitRequestInfo agencyAdmitRequestInfo);

    boolean delete(String objId);

    AgencyAdmitRequestInfo findOne(String objId);

    PageResult<AgencyAdmitRequestView> getPageList(PageParam<AgencyAdmitRequestItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);

    //void save(AgencyAdmitRequestEntity agencyAdmitRequest);
    //AgencyAdmitRequestEntity findOneById(String businessId);
    //void deleteEntity(AgencyAdmitRequestEntity entity);
    //AgencyAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId);
//    void save(AgencyAdmitRequestEntity entity);
    // void save(AgencyAdmitRequestEntity agencyAdmitRequestEntity);
}
