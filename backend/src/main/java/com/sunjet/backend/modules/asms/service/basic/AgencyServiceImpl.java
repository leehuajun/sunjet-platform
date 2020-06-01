package com.sunjet.backend.modules.asms.service.basic;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.AgencyEntity;
import com.sunjet.backend.modules.asms.entity.basic.AgencyWithRegionEntity;
import com.sunjet.backend.modules.asms.entity.basic.view.AgencyView;
import com.sunjet.backend.modules.asms.repository.basic.AgencyRepository;
import com.sunjet.backend.modules.asms.repository.basic.AgencyViewRepository;
import com.sunjet.backend.modules.asms.repository.basic.AgencyWithRegionRepository;
import com.sunjet.backend.modules.asms.repository.basic.RegionRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.AgencyItem;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("agencyService")
public class AgencyServiceImpl implements AgencyService {
    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AgencyViewRepository agencyViewRepository;

    @Autowired
    private AgencyWithRegionRepository agencyWithRegionRepository;

    @Autowired
    private RegionRepository regionRepository;


    /**
     * 保存 实体
     *
     * @param agencyInfo
     * @return
     */
    @Override
    public AgencyInfo save(AgencyInfo agencyInfo) {
        try {

            //if (agencyInfo != null && agencyInfo.getProvinces() != null && agencyInfo.getProvinces().size() > 0) {
            //    for (ProvinceInfo provinceInfo : agencyInfo.getProvinces()) {
            //
            //        AgencyWithRegionEntity agencyWithRegionEntity = agencyWithRegionRepository.findOneByAgencyIdAAndProvinceId(agencyInfo.getObjId(), provinceInfo.getObjId());
            //
            //        if (agencyWithRegionEntity != null) {
            //            // 删除合作商与覆盖省份的关系
            //            this.deleteAgencyWithProvinces(agencyInfo.getObjId(), provinceInfo.getObjId());
            //        } else {
            //
            //
            //        }
            //
            //    }
            //}
            if (agencyInfo != null) {
                // 删除合作商与覆盖省份的关系
                this.deleteAgencyWithProvinces(agencyInfo.getObjId());
            }


            // 重新绑定合作商与覆盖省份的关系
            this.addAgencyWithProvinces(agencyInfo);

            AgencyEntity entity = agencyRepository.save(BeanUtils.copyPropertys(agencyInfo, new AgencyEntity()));
            agencyInfo = BeanUtils.copyPropertys(entity, new AgencyInfo());


            return agencyInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 agencyInfo 对象
     *
     * @param agencyInfo
     * @return
     */
    @Override
    public boolean delete(AgencyInfo agencyInfo) {
        try {
            agencyRepository.delete(agencyInfo.getObjId());
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
    public boolean delete(String objId) {
        try {
            agencyRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public AgencyInfo findOne(String objId) {
        try {
            AgencyEntity agencyEntity = agencyRepository.findOne(objId);
            return BeanUtils.copyPropertys(agencyEntity, new AgencyInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageResult<AgencyView> getPageList(PageParam<AgencyItem> pageParam) {
        //1.查询条件
        AgencyItem agencyItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencyView> specification = null;
        if (agencyItem != null) {
            specification = Specifications.<AgencyView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(agencyItem.getCode()), "code", "%" + agencyItem.getCode() + "%")
                    .like(StringUtils.isNotBlank(agencyItem.getName()), "name", "%" + agencyItem.getName() + "%")
                    .eq(StringUtils.isNotBlank(agencyItem.getProvinceId()), "provinceId", agencyItem.getProvinceId())
                    .eq(StringUtils.isNotBlank(agencyItem.getCityId()), "cityId", agencyItem.getCityId())
                    .build();
        }

        //3.执行查询
        Page<AgencyView> pages = agencyViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换


        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    @Override
    public List<AgencyInfo> findAllByKeyword(String keywordAgency) {
        List<AgencyEntity> entityList = agencyRepository.findAllByKeyword("%" + keywordAgency + "%");
        return entityListToInfoList(entityList);
    }

    /**
     * 通过供货商编号查找
     *
     * @param code
     * @return
     */
    @Override
    public AgencyInfo findOneByCode(String code) {
        try {
            AgencyEntity agencyEntity = agencyRepository.findOneByCode(code);
            return BeanUtils.copyPropertys(agencyEntity, new AgencyInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查code是否存在
     *
     * @param code
     * @return
     */
    @Override
    public Boolean checkCodeExists(String code) {

        AgencyEntity agencyEntity = agencyRepository.findOneByCode(code);
        if (agencyEntity != null)
            return true;
        else
            return false;

    }


    public List<AgencyInfo> entityListToInfoList(List<AgencyEntity> entityList) {

        List<AgencyInfo> infoList = null;

        if (entityList != null) {
            infoList = new ArrayList<>();
            for (AgencyEntity entity : entityList) {
                infoList.add(BeanUtils.copyPropertys(entity, new AgencyInfo()));
            }
        }
        return infoList;
    }


    /**
     * 获取所有合作商
     *
     * @return
     */
    @Override
    public List<AgencyInfo> findAll() {
        List<AgencyInfo> agencyInfoList = new ArrayList<>();
        try {
            List<AgencyEntity> all = agencyRepository.findAll();
            for (AgencyEntity agencyEntity : all) {
                agencyInfoList.add(BeanUtils.copyPropertys(agencyEntity, new AgencyInfo()));
            }
            return agencyInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return agencyInfoList;
        }

    }

    /**
     * 获取所有启用状态的合作商
     *
     * @return
     */
    @Override
    public List<AgencyInfo> findEnabled() {
        List<AgencyInfo> agencyInfoList = new ArrayList<>();
        try {
            List<AgencyEntity> all = agencyRepository.findEnabled();
            for (AgencyEntity agencyEntity : all) {
                agencyInfoList.add(BeanUtils.copyPropertys(agencyEntity, new AgencyInfo()));
            }
            return agencyInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return agencyInfoList;
        }
    }

    /**
     * 保存合作商与覆盖省份的关系
     *
     * @param agencyInfo
     * @return
     */
    @Override
    public boolean addAgencyWithProvinces(AgencyInfo agencyInfo) {
        try {
            if (agencyInfo != null && agencyInfo.getProvinces() != null && agencyInfo.getProvinces().size() > 0) {

                List<AgencyWithRegionEntity> agencyWithRegionEntityList = new ArrayList<>();

                for (ProvinceInfo provinceInfo : agencyInfo.getProvinces()) {

                    AgencyWithRegionEntity entity = new AgencyWithRegionEntity();
                    entity.setAgencyId(agencyInfo.getObjId());
                    entity.setProvinceId(provinceInfo.getObjId());

                    agencyWithRegionEntityList.add(entity);
                }

                agencyWithRegionRepository.save(agencyWithRegionEntityList);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除合作商与覆盖省份的关系
     *
     * @param angencyId
     * @return
     */
    @Override
    public boolean deleteAgencyWithProvinces(String angencyId) {
        try {
            agencyWithRegionRepository.deleteByAgencyId(angencyId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据省id查合作商
     *
     * @param provinceId
     * @return
     */
    @Override
    public List<AgencyInfo> findAllAgencyByProvinceId(String provinceId) {
        List<AgencyEntity> agencyEntityList = agencyRepository.findAllAgencyByProvinceId(provinceId);
        List<AgencyInfo> agencyInfoList = new ArrayList<>();
        for (AgencyEntity agencyEntity : agencyEntityList) {
            agencyInfoList.add(BeanUtils.copyPropertys(agencyEntity, new AgencyInfo()));
        }
        return agencyInfoList;
    }


}
