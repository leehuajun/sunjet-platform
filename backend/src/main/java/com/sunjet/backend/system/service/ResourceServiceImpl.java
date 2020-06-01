package com.sunjet.backend.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.system.entity.OperationEntity;
import com.sunjet.backend.system.entity.ResourceEntity;
import com.sunjet.backend.system.entity.ResourceWithOperationEntity;
import com.sunjet.backend.system.entity.view.ResourceView;
import com.sunjet.backend.system.repository.*;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.dto.system.admin.ResourceInfo;
import com.sunjet.dto.system.admin.ResourceItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 资源管理
 */
@Transactional
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;//资源
    @Autowired
    private ResourceViewRepository resourceViewRepository;//资源视图
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private OperationViewRepository operationViewRepository;//操作视图
    @Autowired
    private ResourceWithOperationRepository resourceWithOperationRepository;
    @Autowired
    private PermissionRepository permissionRepository;//权限
    @Autowired
    private RoleWithPermissionRepository roleWithPermissionRepository;


    /**
     * get one entity
     *
     * @param objId
     * @return
     */
    @Override
    public ResourceInfo findOne(String objId) {
        try {
            ResourceEntity entity = resourceRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new ResourceInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存--entity
     *
     * @param info
     * @return
     */
    @Override
    public ResourceInfo save(ResourceInfo info) {
        try {
            ResourceEntity entity = resourceRepository.save(BeanUtils.copyPropertys(info, new ResourceEntity()));

            //1.删除资源与操作关联关系
            deleteResourceWithOperation(info.getObjId());

            //2.绑定资源与操作的关联关系
            this.saveResourceWithOperation(entity.getObjId(), info.getOperationInfoList());
//            this.saveResourceWithOperation(entity.getCode(),info.getOperationInfoList());

            return BeanUtils.copyPropertys(entity, info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除关联关系
     *
     * @param resourceId
     */
    private void deleteResourceWithOperation(String resourceId) {
        if (StringUtils.isNotBlank(resourceId)) {
            resourceWithOperationRepository.deleteByResource(resourceId);
        }
    }

    /**
     * 绑定关联关系
     *
     * @param operationInfoList
     */
    private void saveResourceWithOperation(String resourceId, List<OperationInfo> operationInfoList) {
        if (operationInfoList != null && operationInfoList.size() > 0) {
            ArrayList<ResourceWithOperationEntity> list = new ArrayList<>();
            for (OperationInfo operationInfo : operationInfoList) {
                ResourceWithOperationEntity entity = new ResourceWithOperationEntity();
                entity.setResId(resourceId);
                entity.setOptId(operationInfo.getObjId());
                list.add(entity);
            }
            resourceWithOperationRepository.save(list);
        }
    }

    /**
     * 删除--by objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {

            ResourceEntity entity = resourceRepository.findOne(objId);
            //删除资源与操作的关联关系
            deleteResourceWithOperation(objId);
            //删除对应的权限
            permissionRepository.deleteAllByCode(entity.getCode() + ":%");
            //删除实体
            resourceRepository.delete(objId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除--entity
     *
     * @param info
     * @return
     */
    @Override
    public boolean delete(ResourceInfo info) {
        try {
            return delete(info.getObjId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<ResourceItem> getPageList(PageParam<ResourceItem> pageParam) {

        //1.查询条件
        ResourceItem resourceItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ResourceView> specification = null;
        if (resourceItem != null) {
            specification = Specifications.<ResourceView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(resourceItem.getName()), "name", "%" + resourceItem.getName() + "%")
                    .build();
        }

        //3.执行查询
        Page<ResourceView> pages = resourceViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //角色关联的操作信息
        HashMap<String, String> map = this.getOperations(pages.getContent());

        //4.数据转换
        List<ResourceItem> rows = new ArrayList<>();
        for (ResourceView view : pages.getContent()) {
            ResourceItem item = new ResourceItem();
            item = BeanUtils.copyPropertys(view, item);
            //关联操作信息
            item.setOperations(map.get(item.getObjId()));
            rows.add(item);
        }

        //5.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 角色关联的操作信息
     *
     * @param viewList
     * @return
     */
    private HashMap<String, String> getOperations(List<ResourceView> viewList) {

        HashMap<String, String> map = null;
        ArrayList<String> list = null;
        if (viewList != null && viewList.size() > 0) {
            list = new ArrayList<>();
            for (ResourceView view : viewList) {
                list.add(view.getObjId());
            }

            List<Object> operations = operationViewRepository.findOperationsByRoleIds(list);
            map = new HashMap<>();
            for (String roleID : list) {

                String operationNames = "";
                for (int i = 0; i < operations.size(); i++) {
                    Object[] m = (Object[]) operations.get(i);

                    String id = m[0] == null ? "" : m[0].toString();
                    if (id.equalsIgnoreCase(roleID)) {
                        if (StringUtils.isNotBlank(operationNames)) {
                            operationNames += ",";
                        }
                        Object name = m[1];
                        operationNames += name == null ? "" : name.toString();
                    }
                }
                map.put(roleID, operationNames);
            }
        }
        return map;
    }

    @Override
    public ResourceInfo findOneWithOperationsById(String objId) {
        try {
            //获取资源实体
            ResourceInfo resourceInfo = BeanUtils.copyPropertys(resourceRepository.findOne(objId), new ResourceInfo());
            //获取资源对应打操作
            List<OperationInfo> operationInfoList = getOperationInfoListByRoleId(objId);
            //绑定操作
            resourceInfo.setOperationInfoList(operationInfoList);

            return resourceInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取资源对应的操作
     *
     * @param resourceId
     * @return
     */
    private List<OperationInfo> getOperationInfoListByRoleId(String resourceId) {
        //获取资源对应的操作集合
        List<OperationEntity> operationEntityList = operationRepository.findAllByResourceId(resourceId);
        //转换后的集合
        List<OperationInfo> operationInfoList = null;
        //转换
        if (operationEntityList != null && operationEntityList.size() > 0) {
            operationInfoList = new ArrayList<>();
            for (OperationEntity entity : operationEntityList) {
                operationInfoList.add(BeanUtils.copyPropertys(entity, new OperationInfo()));
            }
        }

        return operationInfoList;
    }

    /**
     * 获取所有资源
     *
     * @return
     */
    @Override
    public List<ResourceInfo> findAll() {
        try {
            List<ResourceEntity> list = this.resourceRepository.findAll();
            List<ResourceInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (ResourceEntity resourceEntity : list) {
                    infos.add(BeanUtils.copyPropertys(resourceEntity, new ResourceInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有资源 及 资源对应的操作集合
     *
     * @return
     */
    @Override
    public List<ResourceInfo> findAllWithOperations() {
        try {
            List<ResourceEntity> list = resourceRepository.findAll();
            HashMap<String, List<OperationInfo>> operationInfoListMap = getOperationInfoListMap(list);
            List<ResourceInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (ResourceEntity resourceEntity : list) {
                    ResourceInfo info = BeanUtils.copyPropertys(resourceEntity, new ResourceInfo());
                    //获取资源对应打操作
                    //List<OperationInfo> operationInfoList = getOperationInfoListByRoleId(info.getObjId());
                    info.setOperationInfoList(operationInfoListMap.get(info.getObjId()));
                    infos.add(info);
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 组装 资源 对应的 操作 集合
     *
     * @param list
     * @return
     */
    private HashMap<String, List<OperationInfo>> getOperationInfoListMap(List<ResourceEntity> list) {

        //1.获取所有操作
        HashMap<String, OperationInfo> operationInfoMap = getOperationInfoMap();

        //2.获取所有资源与操作的关联关系
        List<ResourceWithOperationEntity> withList = resourceWithOperationRepository.findAll();

        //3.初始化资源容器
        HashMap<String, List<OperationInfo>> operationInfoListMap = getOperationInfoListMapByResource(list);

        //4.资源对应的操作信息
        HashMap<String, List<OperationInfo>> map = new HashMap<>();

        //5.组装资源对应的操作信息
        for (ResourceWithOperationEntity with : withList) {
            String resourceId = with.getResId();

            String operationId = with.getOptId();

            //1.获取资源对应的容器
            List<OperationInfo> infoList = operationInfoListMap.get(resourceId);
            //2.给对应的容器设置值
            infoList.add(operationInfoMap.get(operationId));

            map.put(resourceId, infoList);
        }

        return map;
    }

    /**
     * 初始化资源容器
     *
     * @param list
     * @return
     */
    private HashMap<String, List<OperationInfo>> getOperationInfoListMapByResource(List<ResourceEntity> list) {
        HashMap<String, List<OperationInfo>> map = new HashMap<>();
        for (ResourceEntity entity : list) {
            List<OperationInfo> operationInfoList = new ArrayList<>();
            map.put(entity.getObjId(), operationInfoList);
        }
        return map;
    }

    /**
     * 获取所有操作
     *
     * @return
     */
    private HashMap<String, OperationInfo> getOperationInfoMap() {
        List<OperationEntity> operationEntityList = operationRepository.findAll();
        HashMap<String, OperationInfo> map = new HashMap<>();

        for (OperationEntity entity : operationEntityList) {
            map.put(entity.getObjId(), BeanUtils.copyPropertys(entity, new OperationInfo()));
        }
        return map;
    }

}