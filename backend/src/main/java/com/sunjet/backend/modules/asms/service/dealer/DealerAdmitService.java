package com.sunjet.backend.modules.asms.service.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAdmitRequestView;
import com.sunjet.backend.system.service.BaseService;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 服务站准入申请
 * Created by Administrator on 2016/9/2.
 */
public interface DealerAdmitService extends BaseService {

    List<DealerAdmitRequestInfo> findAll();

    DealerAdmitRequestInfo save(DealerAdmitRequestInfo dealerAdmitRequestInfo);

    DealerAdmitRequestEntity findOneById(String objId);

    boolean delete(DealerAdmitRequestInfo info);

    boolean deleteByObjId(String objId);

    PageResult<DealerAdmitRequestView> getPageList(PageParam<DealerAdmitRequestItem> pageParam);

    Map<String, String> startProcess(Map<String, Object> variables);

    Boolean desertTask(String objId);
}
