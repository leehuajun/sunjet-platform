package com.sunjet.backend.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.system.entity.OperationEntity;
import com.sunjet.backend.system.entity.view.OperationView;
import com.sunjet.backend.system.repository.OperationRepository;
import com.sunjet.backend.system.repository.OperationViewRepository;
import com.sunjet.backend.system.repository.PermissionRepository;
import com.sunjet.backend.system.repository.ResourceWithOperationRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.dto.system.admin.OperationItem;
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
 * 操作列表
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("operationService")
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationViewRepository operationViewRepository;

    @Autowired
    private ResourceWithOperationRepository resourceWithOperationRepository;

    @Autowired
    private PermissionRepository permissionRepository;//权限

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<OperationInfo> findAll() {
        try {
            List<OperationEntity> list = this.operationRepository.findAll();
            List<OperationInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (OperationEntity operationEntity : list) {
                    infos.add(BeanUtils.copyPropertys(operationEntity, new OperationInfo()));
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
    public PageResult<OperationView> getPageList(PageParam<OperationItem> pageParam) {

        //1.查询条件
        OperationItem operationItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<OperationView> specification = null;

        if (operationItem != null) {
            specification = Specifications.<OperationView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(operationItem.getOptName()), "name", operationItem.getOptName())
                    .build();
        }


        //3.执行查询
        Page<OperationView> pages = operationViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<OperationItem> rows = new ArrayList<>();
        //for(OperationView view :pages.getContent()){
        //    OperationItem item = new OperationItem();
        //    item = BeanUtils.copyPropertys(view,item);
        //    rows.add(item);
        //}
        //
        //
        ////5.组装分页信息及集合信息
        //PageResult<OperationItem> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    @Override
    public OperationInfo findOneById(String objId) {
        try {
            OperationEntity entity = operationRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new OperationInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存一个实体
     *
     * @param operationInfo
     * @return返回一个info
     */
    @Override
    public OperationInfo save(OperationInfo operationInfo) {
        try {
            OperationEntity entity = null;
            if (operationInfo.getObjId() == null) {
                entity = operationRepository.save(BeanUtils.copyPropertys(operationInfo, new OperationEntity()));
                return BeanUtils.copyPropertys(entity, new OperationInfo());
            } else {
                entity = BeanUtils.copyPropertys(operationInfo, new OperationEntity());
                operationRepository.save(entity);
                return BeanUtils.copyPropertys(entity, new OperationInfo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除实体
     *
     * @param operationInfo
     * @return结果
     */
    @Override
    public boolean delete(OperationInfo operationInfo) {
        try {
            this.delete(operationInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId删除实体
     *
     * @param objId
     * @return返回实体
     */
    @Override
    public boolean delete(String objId) {
        try {

            OperationEntity entity = operationRepository.findOne(objId);

            //删除关联关系
            resourceWithOperationRepository.deleteByOperation(objId);

            //删除关联权限
            permissionRepository.deleteAllByCode("%:" + entity.getOptCode());

            //删除实体
            operationRepository.delete(objId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<OperationEntity> infoListToEntityList(List<OperationInfo> infoList) {

        List<OperationEntity> entityList = null;
        if (infoList != null && infoList.size() > 0) {

            entityList = new ArrayList<>();

            for (OperationInfo info : infoList) {
                entityList.add(BeanUtils.copyPropertys(info, new OperationEntity()));
            }
        }
        return entityList;
    }

    public List<OperationInfo> entityListToInfoList(List<OperationEntity> entityList) {

        List<OperationInfo> infoList = null;
        if (entityList != null && entityList.size() > 0) {

            infoList = new ArrayList<>();

            for (OperationEntity entity : entityList) {
                infoList.add(BeanUtils.copyPropertys(entity, new OperationInfo()));
            }
        }
        return infoList;
    }
}