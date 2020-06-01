package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.view.DealerView;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.DealerItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by lhj on 16/9/17.
 * 服务站基础信息
 */
public interface DealerService {

    List<DealerInfo> findAll();

    DealerInfo save(DealerInfo dealerInfo);

    DealerInfo findOneById(String objId);

    boolean delete(DealerInfo info);

    boolean deleteByObjId(String objId);

    PageResult<DealerView> getPageList(PageParam<DealerItem> pageParam);

    List<DealerInfo> findAllByKeyword(String keywordDealer);

    List<DealerInfo> findAllParentDealers(String keyword);

    List<DealerInfo> searchDealers(String keyWord, UserInfo userInfo);

    DealerInfo findOneByCode(String dealerCode);

    List<DealerInfo> findAllByServiceManagerId(String serviceManagerId);

    List<DealerInfo> findAllNotServiceManager();

    Boolean checkCodeExists(String dealerCode);
}
