package com.sunjet.backend.modules.asms.service.basic;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.basic.view.DealerView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerViewRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.DealerItem;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/9/17.
 * 服务站基础信息
 */
@Service("dealerService")
public class DealerServiceImpl implements DealerService {


    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private DealerViewRepository dealerViewRepository;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<DealerInfo> findAll() {
        try {
            List<DealerEntity> list = this.dealerRepository.findAll();
            List<DealerInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (DealerEntity dealerEntity : list) {
                    DealerInfo info = new DealerInfo();
                    infos.add(BeanUtils.copyPropertys(dealerEntity, info));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<DealerView> getPageList(PageParam<DealerItem> pageParam) {

        //1.查询条件
        DealerItem dealerItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerView> specification = null;
        if (dealerItem != null) {
            specification = Specifications.<DealerView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(dealerItem.getName()), "name", "%" + dealerItem.getName() + "%")
                    .like(StringUtils.isNotBlank(dealerItem.getCode()), "code", "%" + dealerItem.getCode() + "%")
                    .eq(StringUtils.isNotBlank(dealerItem.getStar()), "star", dealerItem.getStar())
                    .eq(StringUtils.isNotBlank(dealerItem.getParentId()), "parentId", dealerItem.getParentId())
                    .like(StringUtils.isNotBlank(dealerItem.getServiceManagerName()), "serviceManagerName", "%" + dealerItem.getServiceManagerName() + "%")
                    .eq(StringUtils.isNotBlank(dealerItem.getLevel()), "level", dealerItem.getLevel())
                    .eq(StringUtils.isNotBlank(dealerItem.getProvinceId()), "provinceId", dealerItem.getProvinceId())
                    .eq(StringUtils.isNotBlank(dealerItem.getCityId()), "cityId", dealerItem.getCityId())
                    //.eq(StringUtils.isNotBlank(dealerInfo.getServiceManager()), "cityId", dealerInfo.getCityId())

                    .build();
        }


        //3.执行查询
        Page<DealerView> pages = dealerViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

//        //4.数据转换
//        List<DealerInfo> rows = new ArrayList<>();
//        for(DealerEntity entity :pages.getContent()){
//            DealerInfo info = entityToInfo(entity);
//            rows.add(info);
//        }


        //5.组装分页信息及集合信息
        //PageResult<DealerInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 搜索服务站
     *
     * @param keywordDealer
     * @return
     */
    @Override
    public List<DealerInfo> findAllByKeyword(String keywordDealer) {

        List<DealerEntity> entityList = dealerRepository.findAllByKeyword("%" + keywordDealer + "%");

        return entityListToInfoList(entityList);
    }

    /**
     * 通过objId查一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public DealerInfo findOneById(String objId) {
        try {
            DealerEntity entity = dealerRepository.findOne(objId);
            DealerInfo info = new DealerInfo();
            return BeanUtils.copyPropertys(entity, info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param dealerInfo
     * @return
     */
    @Override
    public DealerInfo save(DealerInfo dealerInfo) {
        try {
            DealerEntity entity = new DealerEntity();
            dealerRepository.save(BeanUtils.copyPropertys(dealerInfo, entity));
            return BeanUtils.copyPropertys(entity, dealerInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 dealerInfo 对象
     *
     * @param dealerInfo
     * @return
     */
    @Override
    public boolean delete(DealerInfo dealerInfo) {
        try {
            DealerEntity entity = new DealerEntity();
            entity = BeanUtils.copyPropertys(dealerInfo, entity);
            dealerRepository.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByObjId(String objId) {
        try {
            dealerRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 通过关键字查询所有父节点服务站
     *
     * @return
     */
    @Override
    public List<DealerInfo> findAllParentDealers(String keyword) {
        List<DealerEntity> allParentDealers = dealerRepository.findAllParentDealers("%" + keyword + "%");
        List<DealerInfo> dealerInfos = new ArrayList<>();
        for (DealerEntity allParentDealer : allParentDealers) {
            DealerInfo info = new DealerInfo();
            dealerInfos.add(BeanUtils.copyPropertys(allParentDealer, info));
        }

        return dealerInfos;
    }

    /**
     * 根据用户和关键字搜索服务站
     *
     * @param keyword
     */
    @Override
    public List<DealerInfo> searchDealers(String keyword, UserInfo userInfo) {
        List<DealerEntity> dealers = new ArrayList<>();
        if (userInfo.getDealer() != null) {   // 服务站用户
            if (userInfo.getDealer().getParentId() != null) {  // 是二级服务站
                DealerEntity entity = new DealerEntity();
                dealers.clear();
                dealers.add(BeanUtils.copyPropertys(userInfo.getDealer(), entity));  // 二级服务站只能看自己的
            } else {   // 一级服务站
                DealerEntity entity = new DealerEntity();
                dealers = dealerRepository.findChildrenByParentIdAndFilter(userInfo.getDealer().getObjId(), "%" + keyword.trim() + "%");
                dealers.add(BeanUtils.copyPropertys(userInfo.getDealer(), entity));
            }
        } else if (userInfo.getAgency() != null) {   // 合作商
            dealers = dealerRepository.findAllByKeyword("%" + keyword.trim() + "%");
        } else {   // 五菱用户
            //UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
            List<RoleInfo> roles = userInfo.getRoles();
            boolean Permissions = false;
            if (userInfo.getRoles() != null) {
                for (RoleInfo role : roles) {
                    //role000 服务经理
                    if (role.getRoleId().equals("role000")) {
                        Permissions = true;
                    }
                }
            }

            if (Permissions) {
                //服务经理
                dealers = dealerRepository.findAllByKeywordAndServiceManager(userInfo.getObjId(), "%" + keyword.trim() + "%");
            } else {
                //五菱其他用户
                dealers = dealerRepository.findAllByKeyword("%" + keyword.trim() + "%");
            }
        }
        List<DealerInfo> dealerInfos = new ArrayList<>();
        for (DealerEntity dealer : dealers) {
            DealerInfo info = new DealerInfo();
            dealerInfos.add(BeanUtils.copyPropertys(dealer, info));
        }

        return dealerInfos;

    }

    /**
     * 根据条件查询服务站信息
     *
     * @param dealerCode
     * @return
     */
    @Override
    public DealerInfo findOneByCode(String dealerCode) {
        try {
            DealerEntity oneByCode = dealerRepository.findOneByCode(dealerCode);
            return BeanUtils.copyPropertys(oneByCode, new DealerInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DealerInfo> findAllByServiceManagerId(String serviceManagerId) {
        List<DealerInfo> dealerInfos = new ArrayList<>();
        try {
            List<DealerEntity> dealerEntities = dealerRepository.findAllByServiceManagerId(serviceManagerId);
            for (DealerEntity dealerEntity : dealerEntities) {
                dealerInfos.add(BeanUtils.copyPropertys(dealerEntity, new DealerInfo()));
            }

            return dealerInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return dealerInfos;
        }
    }

    /**
     * 查询没有绑定服务经理的服务站
     *
     * @return
     */
    @Override
    public List<DealerInfo> findAllNotServiceManager() {
        List<DealerEntity> dealerEntities = dealerRepository.findAllNotServiceManager();
        return entityListToInfoList(dealerEntities);
    }


    public List<DealerInfo> entityListToInfoList(List<DealerEntity> entities) {

        List<DealerInfo> infos = new ArrayList<>();
        if (entities != null) {
            infos = new ArrayList<>();
            for (DealerEntity entity : entities) {
                DealerInfo info = new DealerInfo();
                infos.add(BeanUtils.copyPropertys(entity, info));
            }
        }
        return infos;

    }

    /**
     * 检查code是否存在
     *
     * @param dealerCode
     * @return
     */
    @Override
    public Boolean checkCodeExists(String dealerCode) {
        DealerEntity dealerEntity = dealerRepository.findOneByCode(dealerCode);
        if (dealerEntity != null) {
            return true;
        } else {
            return false;
        }

    }

}
