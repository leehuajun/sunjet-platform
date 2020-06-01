package com.sunjet.backend.modules.asms.service.agency;


import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAlterRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.agency.AgencyAlterRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAlterRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyAlterService extends BaseService {

    AgencyAlterRequestInfo save(AgencyAlterRequestInfo agencyAlterRequestInfo);

    boolean delete(AgencyAlterRequestInfo agencyAlterRequestInfo);

    boolean delete(String objId);

    AgencyAlterRequestInfo findOne(String objId);

    PageResult<AgencyAlterRequestView> getPageList(PageParam<AgencyAlterRequestItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> map);

    Boolean desertTask(String objId);


    //void save(AgencyAlterRequestEntity agencyAlterRequest);
    //AgencyAlterRequestEntity findOneById(String businessId);
    //void deleteEntity(AgencyAlterRequestEntity entity);
    // void save(AgencyAlterRequestEntity entity);
    //  void save(AgencyAlterRequestEntity agencyAlterRequestEntity);

}
