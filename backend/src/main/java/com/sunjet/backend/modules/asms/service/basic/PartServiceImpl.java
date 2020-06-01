package com.sunjet.backend.modules.asms.service.basic;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import com.sunjet.backend.modules.asms.repository.basic.PartRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.basic.PartInfoExt;
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
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/10/27.
 * 配件基础信息
 */

@Transactional
@Service("partService")
public class PartServiceImpl implements PartService {
    @Autowired
    private PartRepository partRepository;

    /**
     * 保存 实体
     *
     * @param partInfo
     * @return
     */
    @Override
    public PartInfo save(PartInfo partInfo) {
        try {
            PartEntity entity = partRepository.save(BeanUtils.copyPropertys(partInfo, new PartEntity()));
            return BeanUtils.copyPropertys(entity, new PartInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            partRepository.delete(objId);
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
    public PartInfo findOne(String objId) {
        try {
            PartEntity entity = partRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new PartInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 去掉空白字符串
     *
     * @param str
     * @return
     */
    private String StringTrim(String str) {
        if (str != null) {
            str = str.replaceAll(" ", "").replace("   ", "");
            if (str == null) {
                str = "";
            }
        }
        return str;
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<PartInfo> getPageList(PageParam<PartInfo> pageParam) {
        //1.查询条件
        PartInfo partInfo = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PartEntity> specification = null;
        if (partInfo != null) {

            specification = Specifications.<PartEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(partInfo.getCode()), "code", "%" + StringTrim(partInfo.getCode()) + "%")
                    .like(StringUtils.isNotBlank(partInfo.getName()), "name", "%" + StringTrim(partInfo.getName()) + "%")
                    .build();
        }

        //3.执行查询
        Page<PartEntity> pages = partRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<PartInfo> rows = new ArrayList<>();
        for (PartEntity entity : pages.getContent()) {
            PartInfo info = BeanUtils.copyPropertys(entity, new PartInfo());
            rows.add(info);
        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(rows, pages, pageParam);

    }

    /**
     * 获取所有配件
     *
     * @return
     */
    @Override
    public List<PartInfo> findAll() {
        try {
            List<PartEntity> list = this.partRepository.findAll();
            List<PartInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (PartEntity entity : list) {
                    infos.add(BeanUtils.copyPropertys(entity, new PartInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 条件查询
     *
     * @param keyword
     * @return
     */
    @Override
    public List<PartInfo> findAllByKeyword(String keyword) {
        try {
            List<PartEntity> entityList = partRepository.findAllByKeyword("%" + keyword + "%");
            List<PartInfo> infoList = null;
            if (entityList != null) {
                infoList = new ArrayList<>();
                for (PartEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new PartInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过onbId 查找一个速报配件实体
     *
     * @param objIds
     * @return
     */
    @Override
    public List<PartInfo> findByPartId(ArrayList<String> objIds) {
        try {
            List<PartEntity> byPartId = partRepository.findByPartId(objIds);
            List<PartInfo> partInfoList = new ArrayList<>();
            if (byPartId != null) {
                for (PartEntity entity : byPartId) {
                    PartInfo info = BeanUtils.copyPropertys(entity, new PartInfo());
                    partInfoList.add(info);
                }
            }
            return partInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional(value = Transactional.TxType.NEVER)
    @Override
    public List<PartInfoExt> importParts(List<PartInfoExt> infoExts) {
        List<PartInfoExt> incorrectInfos = new ArrayList<>();

        infoExts.forEach(info -> {
            try {
                PartEntity entity = BeanUtils.copyPropertys(info, new PartEntity());
                //配件类型为空不能导入
                if (StringUtils.isBlank(entity.getPartType())) {
                    info.setErr(3);
                    incorrectInfos.add(info);
                } else {
                    partRepository.save(entity);
                }

            } catch (Exception ex) {
                info.setErr(1);
                incorrectInfos.add(info);
            }
        });

        return incorrectInfos;
    }

    @Transactional(value = Transactional.TxType.NEVER)
    @Override
    public List<PartInfoExt> modifyParts(List<PartInfoExt> infoExts) {
        List<PartInfoExt> incorrectInfos = new ArrayList<>();

        List<String> codes = infoExts.stream().map(p -> p.getCode()).collect(Collectors.toList());
        System.out.println(codes);

        List<PartEntity> entities = partRepository.findAllByCodeIn(codes);
        infoExts.forEach(infoExt -> {
            List<PartEntity> list = entities.stream().filter(e -> e.getCode().equals(infoExt.getCode())).collect(Collectors.toList());
            if (list == null || list.size() <= 0) {
                infoExt.setErr(1);
                incorrectInfos.add(infoExt);
            } else {
                PartEntity entity = list.get(0);
                //如果配件类型为空
                if (StringUtils.isNotBlank(infoExt.getPartType())) {
                    entity.setModifierId(infoExt.getModifierId());
                    entity.setModifierName(infoExt.getModifierName());
                    entity.setName(infoExt.getName());
                    if (StringUtils.isNotBlank(entity.getWarrantyTime())) {
                        entity.setWarrantyTime(infoExt.getWarrantyTime());
                    }
                    if (StringUtils.isNotBlank(entity.getWarrantyMileage())) {
                        entity.setWarrantyMileage(infoExt.getWarrantyMileage());
                    }
                    entity.setPrice(infoExt.getPrice());
                    entity.setPartType(infoExt.getPartType());
                    try {
                        partRepository.save(entity);
                    } catch (Exception e) {
                        infoExt.setErr(2);
                        incorrectInfos.add(infoExt);
                    }
                } else {
                    infoExt.setErr(3);
                    incorrectInfos.add(infoExt);
                }

            }
        });

        return incorrectInfos;
    }

    /**
     * 通过code 查part
     *
     * @param code
     * @return
     */
    @Override
    public PartEntity findOneByCode(String code) {

        return partRepository.findOneByCode(code);
    }

    /**
     * 配件搜索
     *
     * @param code
     * @return
     */
    @Override
    public List<PartEntity> findAllByCode(String code) {
        return partRepository.findAllByCode(code);
    }
}
