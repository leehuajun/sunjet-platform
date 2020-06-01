package com.sunjet.backend.system.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.AgencyEntity;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.repository.basic.AgencyRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.system.entity.RoleEntity;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.entity.UserWithRoleEntity;
import com.sunjet.backend.system.entity.view.UserView;
import com.sunjet.backend.system.repository.*;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.admin.UserItem;
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
 * @author: lhj
 * @create: 2017-07-02 00:51
 * @description: 说明
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserViewRepository userViewRepository;
    @Autowired
    private RoleViewRepository roleViewRepository;
    @Autowired
    private UserWithRoleRepository userWithRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private AgencyRepository agencyRepository;


    /**
     * get userInfo by logId
     *
     * @param logId
     * @return
     */
    @Override
    public UserInfo findOneByLogId(String logId) {
        try {
            UserEntity userEntity = userRepository.findOneByLogId(logId);
            return BeanUtils.copyPropertys(userEntity, new UserInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get userInfo list
     *
     * @return
     */
    @Override
    public List<UserInfo> findAll() {
        try {
            List<UserEntity> list = userRepository.findAll();
            List<UserInfo> users = new ArrayList<>();
            for (UserEntity userEntity : list) {
                users.add(BeanUtils.copyPropertys(userEntity, new UserInfo()));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get entity by id
     *
     * @param objId
     * @return
     */
    @Override
    public UserInfo findOne(String objId) {
        try {
            UserEntity entity = userRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new UserInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增/修改
     *
     * @param info
     * @return
     */
    @Override
    public UserInfo save(UserInfo info) {
        try {
            UserEntity entity = BeanUtils.copyPropertys(info, new UserEntity());
            String dealerId = "";//服务站
            if (info.getDealer() != null) {
                dealerId = info.getDealer().getObjId();
                entity.setDealerId(dealerId);
                entity.setAgencyId("");
            }
            String agencyId = "";//合作商
            if (info.getAgency() != null) {
                agencyId = info.getAgency().getObjId();
                entity.setAgencyId(agencyId);
                entity.setDealerId("");
            }
            //1.保存实体
            entity = userRepository.save(entity);
            //2.删除用户与角色的关联关系
            removeUsersFromRole(entity.getObjId());
            //3.绑定 用户 与 角色 的关联关系
            info.setObjId(entity.getObjId());
            this.addUsersToRole(info);
            return BeanUtils.copyPropertys(entity, info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除用户与角色的关联关系
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeUsersFromRole(String userId) {
        try {
            userWithRoleRepository.deleteByUser(userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 修改密码
     *
     * @param userInfo
     * @return
     */
    @Override
    public UserInfo changePassword(UserInfo userInfo) {
        try {
            UserEntity entity = userRepository.save(BeanUtils.copyPropertys(userInfo, new UserEntity()));
            return BeanUtils.copyPropertys(entity, new UserInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过userid 获取用户名
     *
     * @param userId
     * @return
     */
    @Override
    public String findOneWithUserId(String userId) {
        try {
            UserEntity userEntity = userRepository.findOneByLogId(userId);
            return userEntity.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取所有的服务经理
     *
     * @return
     */
    @Override
    public List<UserInfo> findAllByRoleName(String roleName) {
        try {
            RoleEntity roleEntity = roleRepository.findOneByRoleName(roleName);
            List<UserInfo> userInfoList = findAllByRoleId(roleEntity.getObjId());
            return userInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取登陆用户
     *
     * @param logId
     * @return
     */
    @Override
    public UserInfo findOneByEnabledLogId(String logId) {
        UserEntity userEntity = userRepository.findOneByEnabledLogId(logId);
        UserInfo userInfo = null;
        if (userEntity != null) {
            //服务站不为null
            if (StringUtils.isNotBlank(userEntity.getDealerId())) {
                DealerEntity dealerEntity = dealerRepository.findOne(userEntity.getDealerId());
                if (dealerEntity.getEnabled()) {
                    userInfo = BeanUtils.copyPropertys(userEntity, new UserInfo());
                }
            } else if (StringUtils.isNotBlank(userEntity.getAgencyId())) {
                //合作商不为空
                AgencyEntity agencyEntity = agencyRepository.findOne(userEntity.getAgencyId());
                if (agencyEntity.getEnabled()) {
                    userInfo = BeanUtils.copyPropertys(userEntity, new UserInfo());
                }
            } else if ("wuling".equals(userEntity.getUserType()) || "admin".equals(userEntity.getLogId())) {
                userInfo = BeanUtils.copyPropertys(userEntity, new UserInfo());
            }
        }
        return userInfo;
    }

    /**
     * 绑定 用户 与 角色 的关联关系
     *
     * @param info
     * @return
     */
    @Override
    public boolean addUsersToRole(UserInfo info) {
        try {
            if (info != null && info.getRoles() != null && info.getRoles().size() > 0) {

                List<UserWithRoleEntity> userWithRoleEntityList = new ArrayList<>();
                for (RoleInfo roleInfo : info.getRoles()) {

                    UserWithRoleEntity entity = new UserWithRoleEntity();

                    entity.setRoleId(roleInfo.getObjId());//角色id
                    entity.setUserId(info.getObjId());//用户id

                    userWithRoleEntityList.add(entity);
                }

                userWithRoleRepository.save(userWithRoleEntityList);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 -- by id
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            //删除 用户 与 角色 的关联关系
            this.removeUsersFromRole(objId);

            //删除实体
            userRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除--对象
     *
     * @param info
     * @return
     */
    @Override
    public boolean delete(UserInfo info) {
        try {
            this.delete(info.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<UserItem> getPageList(PageParam<UserItem> pageParam) {
        //1.查询条件
        UserItem userItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<UserView> specification = null;
        if (StringUtils.isNotBlank(userItem.getName())) {
            specification = Specifications.<UserView>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(userItem.getName()), "name", "%" + userItem.getName() + "%")
                    .like(StringUtils.isNotEmpty(userItem.getName()), "logId", "%" + userItem.getName() + "%")
                    .build();
        }
        //3.执行查询
        Page<UserView> pages = userViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //用户关联的角色信息信息
        HashMap<String, String> map = getOperations(pages.getContent());

        //4.数据转换
        List<UserItem> rows = new ArrayList<>();
        for (UserView view : pages.getContent()) {
            UserItem item = new UserItem();
            item = BeanUtils.copyPropertys(view, item);
            //绑定角色信息
            item.setRoleNames(map.get(item.getObjId()));
            rows.add(item);
        }
        //5.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 用户关联的角色信息信息
     *
     * @param viewList
     * @return
     */
    private HashMap<String, String> getOperations(List<UserView> viewList) {

        HashMap<String, String> map = null;
        ArrayList<String> list = null;
        if (viewList != null && viewList.size() > 0) {
            list = new ArrayList<>();
            for (UserView view : viewList) {
                list.add(view.getObjId());
            }
            List<Object> roles = roleViewRepository.findRolesByUserIds(list);
            map = new HashMap<>();
            for (String objId : list) {

                String roleNames = "";
                for (int i = 0; i < roles.size(); i++) {
                    Object[] m = (Object[]) roles.get(i);

                    String id = m[0].toString();
                    if (id.equalsIgnoreCase(objId)) {
                        if (StringUtils.isNotBlank(roleNames)) {
                            roleNames += ",";
                        }
                        Object name = m[1];
                        roleNames += name == null ? "" : name.toString();
                    }
                }
                map.put(objId, roleNames);
            }
        }
        return map;
    }


    /**
     * 通过roleId获取所有用户
     *
     * @param roleId
     * @return
     */
    @Override
    public List<UserInfo> findAllByRoleId(String roleId) {
        List<UserEntity> userEntityList = userRepository.findAllByRoleId(roleId);

        List<UserInfo> userInfoList = null;
        if (userEntityList != null && userEntityList.size() > 0) {
            userInfoList = new ArrayList<>();
            for (UserEntity entity : userEntityList) {
                userInfoList.add(BeanUtils.copyPropertys(entity, new UserInfo()));
            }
        }
        return userInfoList;
    }

}
