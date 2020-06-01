package com.sunjet.backend.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.system.entity.ConfigEntity;
import com.sunjet.backend.system.repository.ConfigRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.ConfigInfo;
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
 * Created by lhj on 16/6/17.
 * 系统配置信息
 */
@Transactional
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    /**
     * 获取所有参数设置列表
     *
     * @return
     */
    @Override
    public List<ConfigInfo> findAll() {
        try {
            List<ConfigEntity> list = this.configRepository.findAll();
            List<ConfigInfo> infos = new ArrayList<>();
            ;
            if (list != null && list.size() > 0) {
                for (ConfigEntity configEntity : list) {
                    infos.add(BeanUtils.copyPropertys(configEntity, new ConfigInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 恢复默认值
     *
     * @param configInfo
     */
    @Override
    public void restore(ConfigInfo configInfo) {

        //ConfigEntity model = configRepository.findOne(configEntity.getObjId());
        //model.setConfigValue(model.getConfigDefaultValue());
        //configRepository.save(model);
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    @Override
    public String getValueByKey(String key) {
        //if(CacheManager.getConfigKeySet().size()<=0){
        //    customCacheManager.initConfig();
        //}
        //return CacheManager.getConfigValue("frigid_subsidy");
        try {
            String valueByKey = configRepository.getValueByKey(key);
            return valueByKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param configInfo
     * @return
     */
    @Override
    public ConfigInfo save(ConfigInfo configInfo) {
        try {
            ConfigEntity entity = configRepository.save(BeanUtils.copyPropertys(configInfo, new ConfigEntity()));
            return BeanUtils.copyPropertys(entity, new ConfigInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 configInfo 对象
     *
     * @param configInfo
     * @return
     */
    @Override
    public boolean delete(ConfigInfo configInfo) {
        try {
            ConfigEntity entity = BeanUtils.copyPropertys(configInfo, new ConfigEntity());
            configRepository.delete(entity);
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
            configRepository.delete(objId);
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
    public ConfigInfo findOne(String objId) {
        try {
            ConfigEntity entity = configRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new ConfigInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<ConfigInfo> getPageList(PageParam<ConfigInfo> pageParam) {
        //1.查询条件
        ConfigInfo configInfo = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ConfigEntity> specification = null;
        if (configInfo != null) {
            specification = Specifications.<ConfigEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(configInfo.getConfigKey()), "configKey", configInfo.getConfigKey())
                    .build();
        }

        //3.执行查询
        Page<ConfigEntity> pages = configRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<ConfigInfo> rows = new ArrayList<>();
        for (ConfigEntity entity : pages.getContent()) {
            ConfigInfo info = BeanUtils.copyPropertys(entity, new ConfigInfo());
            rows.add(info);
        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 获取所有配置
     *
     * @return
     */
    @Override
    public List<ConfigInfo> getAllConfig() {

        List<ConfigInfo> configInfoList = null;

        List<ConfigEntity> list = configRepository.findAll();

        if (list != null && list.size() > 0) {
            configInfoList = new ArrayList<>();
            for (ConfigEntity entity : list) {
                ConfigInfo info = BeanUtils.copyPropertys(entity, new ConfigInfo());
                configInfoList.add(info);
            }
        }
        return configInfoList;
    }


//    /**
//     * info 转为 entity
//     *
//     * @param configInfo
//     * @return
//     */
//    private ConfigEntity infoToEntity(ConfigInfo configInfo) {
//        return ConfigEntity.ConfigEntityBuilder
//                .aConfigEntity()
//                .withConfigKey(configInfo.getConfigKey())
//                .withConfigValue(configInfo.getConfigValue())
//                .withConfigDefaultValue(configInfo.getConfigDefaultValue())
//                .withComment(configInfo.getComment())
//                .withObjId(configInfo.getObjId())
//                .withCreaterName(configInfo.getCreaterName())
//                .withModifierName(configInfo.getModifierName())
//                //.withCreatedTime(configInfo.getCreatedTime())
//                //.withModifiedTime(configInfo.getModifiedTime())
//                .build();
//    }
//
//    /**
//     * Entity 转为 info
//     * @param configEntity
//     * @return
//     */
//    private ConfigInfo entityToInfo(ConfigEntity configEntity) {
//        return ConfigInfo.ConfigInfoBuilder
//                .aConfigInfo()
//                .withObjId(configEntity.getObjId())
//                .withConfigKey(configEntity.getConfigKey())
//                .withConfigValue(configEntity.getConfigValue())
//                .withConfigDefaultValue(configEntity.getConfigDefaultValue())
//                .withComment(configEntity.getComment())
//                .withCreaterName(configEntity.getCreaterName())
//                .withModifierName(configEntity.getModifierName())
//                //.withCreatedTime(configEntity.getCreatedTime())
//                //.withModifiedTime(configEntity.getModifiedTime())
//                .build();
//    }

}