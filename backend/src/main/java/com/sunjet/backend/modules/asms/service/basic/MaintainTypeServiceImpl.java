package com.sunjet.backend.modules.asms.service.basic;


import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.MaintainEntity;
import com.sunjet.backend.modules.asms.entity.basic.MaintainTypeEntity;
import com.sunjet.backend.modules.asms.repository.basic.MaintainRepository;
import com.sunjet.backend.modules.asms.repository.basic.MaintainTypeRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
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
 * Created by Administrator on 2016/9/12.
 * 项目工时定额
 */
@Transactional
@Service("maintainTypeService")
public class MaintainTypeServiceImpl implements MaintainTypeService {
    @Autowired
    private MaintainTypeRepository maintainTypeRepository;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<MaintainTypeInfo> findAll() {
        try {
            List<MaintainTypeEntity> list = this.maintainTypeRepository.findAll();
            List<MaintainTypeInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (MaintainTypeEntity maintainTypeEntity : list) {
                    infos.add(BeanUtils.copyPropertys(maintainTypeEntity, new MaintainTypeInfo()));
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
    public PageResult<MaintainTypeInfo> getPageList(PageParam<MaintainTypeInfo> pageParam) {

        //1.查询条件
        MaintainTypeInfo maintainTypeInfo = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<MaintainTypeEntity> specification = null;
        if (maintainTypeInfo != null) {
            specification = Specifications.<MaintainTypeEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(maintainTypeInfo.getName()), "name", "%" + maintainTypeInfo.getName() + "%")
//                    .like(StringUtils.isNotEmpty(maintainTypeInfo.getCode()), "code", "%" + maintainTypeInfo.getCode() + "%")
                    .build();
        }


        //3.执行查询
        Page<MaintainTypeEntity> pages = maintainTypeRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<MaintainTypeInfo> rows = new ArrayList<>();
        for (MaintainTypeEntity entity : pages.getContent()) {
//            MaintainTypeInfo info = EntityToInfo(entity);
            rows.add(BeanUtils.copyPropertys(entity, new MaintainTypeInfo()));
        }


        //5.组装分页信息及集合信息
        //PageResult<MaintainInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

//    @Override
//    public MaintainTypeInfo findOneById(String objId) {
//        try {
//            MaintainTypeEntity entity = maintainTypeRepository.findOne(objId);
//            return EntityToInfo(entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 保存 实体
     *
     * @param maintainTypeInfo
     * @return
     */
    @Override
    public MaintainTypeInfo save(MaintainTypeInfo maintainTypeInfo) {
        try {
//            MaintainTypeEntity entity = maintainTypeRepository.save(infoToEntity(maintainTypeInfo));
            MaintainTypeEntity entity = maintainTypeRepository.save(BeanUtils.copyPropertys(maintainTypeInfo, new MaintainTypeEntity()));
            return BeanUtils.copyPropertys(entity, new MaintainTypeInfo());
//            return EntityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 maintainInfo 对象
     *
     * @param maintainTypeInfo
     * @return
     */
    @Override
    public boolean delete(MaintainTypeInfo maintainTypeInfo) {
        try {
//            MaintainTypeEntity entity = infoToEntity(maintainTypeInfo);

            maintainTypeRepository.delete(BeanUtils.copyPropertys(maintainTypeInfo, new MaintainTypeEntity()));
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
            maintainTypeRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MaintainTypeInfo> findModels() {
        try {
            List<MaintainTypeEntity> list = maintainTypeRepository.findModels();
            List<MaintainTypeInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (MaintainTypeEntity maintainTypeEntity : list) {
//                    infos.add(EntityToInfo(maintainTypeEntity));
                    infos.add(BeanUtils.copyPropertys(maintainTypeEntity, new MaintainTypeInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MaintainTypeInfo> findSystems(String parentId) {
        try {
            List<MaintainTypeEntity> list = maintainTypeRepository.findSystems(parentId);
            List<MaintainTypeInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (MaintainTypeEntity maintainTypeEntity : list) {
//                    infos.add(EntityToInfo(maintainTypeEntity));
                    infos.add(BeanUtils.copyPropertys(maintainTypeEntity, new MaintainTypeInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MaintainTypeInfo> findSubSystems(String parentId) {
        try {
            List<MaintainTypeEntity> list = maintainTypeRepository.findSubSystems(parentId);
            List<MaintainTypeInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (MaintainTypeEntity maintainTypeEntity : list) {
//                    infos.add(EntityToInfo(maintainTypeEntity));
                    infos.add(BeanUtils.copyPropertys(maintainTypeEntity, new MaintainTypeInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MaintainTypeInfo> findAllByParentId(String parentId) {
        try {
            List<MaintainTypeEntity> list = maintainTypeRepository.findAllByParentId(parentId);
            List<MaintainTypeInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (MaintainTypeEntity maintainTypeEntity : list) {
                    infos.add(BeanUtils.copyPropertys(maintainTypeEntity, new MaintainTypeInfo()));
//                    infos.add(EntityToInfo(maintainTypeEntity));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public MaintainTypeInfo findOne(String objId) {
        try {
            MaintainTypeEntity maintainTypeEntity = maintainTypeRepository.findOne(objId);
            return BeanUtils.copyPropertys(maintainTypeEntity, new MaintainTypeInfo());
//            return EntityToInfo(maintainTypeEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
