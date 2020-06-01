package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.DictionaryEntity;
import com.sunjet.backend.system.repository.DictionaryRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.system.admin.DictionaryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-18 下午2:50
 * 数据字典
 */
@Service("dictionaryService")
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    /**
     * 保存一个实体
     *
     * @param dictionaryInfo
     * @return
     */
    @Override
    public DictionaryInfo save(DictionaryInfo dictionaryInfo) {
        try {
            DictionaryEntity entity = dictionaryRepository.save(infoToEntity(dictionaryInfo));
            return entityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 -->  通过对象
     *
     * @param dictionaryInfo
     * @return
     */
    @Override
    public boolean delete(DictionaryInfo dictionaryInfo) {
        try {
            DictionaryEntity entity = infoToEntity(dictionaryInfo);
            dictionaryRepository.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 删除 -->  通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            dictionaryRepository.delete(objId);
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
    public DictionaryInfo findOne(String objId) {
        try {
            DictionaryEntity entity = dictionaryRepository.findOne(objId);
            return entityToInfo(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查找字典列表
     *
     * @return
     */
    @Override
    public List<DictionaryInfo> findAll() {
        try {
            List<DictionaryEntity> dictionaryEntityList = dictionaryRepository.findAll();
            List<DictionaryInfo> dictionaryInfoList = null;
            dictionaryInfoList = new ArrayList<>();
            if (dictionaryEntityList != null && dictionaryEntityList.size() > 0) {
                for (DictionaryEntity dictionaryEntity : dictionaryEntityList) {
                    dictionaryInfoList.add(entityToInfo(dictionaryEntity));
                }
            }
            return dictionaryInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查找所以父级的所有数据
     *
     * @return
     */
    @Override
    public List<DictionaryInfo> findAllParent() {
        try {
            List<DictionaryEntity> allParent = dictionaryRepository.findAllParent();
            List<DictionaryInfo> dictionaryInfoList = null;
            dictionaryInfoList = new ArrayList<>();
            for (DictionaryEntity dictionaryEntity : allParent) {
                dictionaryInfoList.add(entityToInfo(dictionaryEntity));
            }
            return dictionaryInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DictionaryInfo> findDictionariesByParentCode(String code) {

//        DictionaryEntity entity = dictionaryRepository.findDictionaryByCode(code);
//        List<DictionaryEntity> List = dictionaryRepository.findListByParentId(entity.getObjId());
        List<DictionaryEntity> List = dictionaryRepository.findListByParentCode(code);
        return entityListToInfoList(List);
    }

    /**
     * 根据编码code查找一条基础数据
     *
     * @param code
     * @return
     */
    @Override
    public DictionaryInfo findDictionaryByCode(String code) {
        try {
            DictionaryEntity dictionaryEntity = dictionaryRepository.findDictionaryByCode(code);
            return BeanUtils.copyPropertys(dictionaryEntity, new DictionaryInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<DictionaryInfo> entityListToInfoList(List<DictionaryEntity> entities) {

        List<DictionaryInfo> infos = null;
        if (entities != null) {
            infos = new ArrayList<>();
            for (DictionaryEntity entity : entities) {
                infos.add(entityToInfo(entity));
            }
        }
        return infos;

    }

    /**
     * info 转为 entity
     *
     * @param dictionaryInfo
     * @return
     */
    private DictionaryEntity infoToEntity(DictionaryInfo dictionaryInfo) {
        DictionaryEntity entity = null;
        if (dictionaryInfo.getParent() != null) {
            entity = infoToEntity(dictionaryInfo.getParent());
        }

        return DictionaryEntity.DictionaryEntityBuilder
                .aDictionaryEntity()
                .withCreaterId(dictionaryInfo.getCreaterId())
                .withCreaterName(dictionaryInfo.getCreaterName())
                .withModifierId(dictionaryInfo.getModifierId())
                .withName(dictionaryInfo.getName())
                .withModifierName(dictionaryInfo.getModifierName())
                .withCode(dictionaryInfo.getCode())
                .withObjId(dictionaryInfo.getObjId())
                .withSeq(dictionaryInfo.getSeq())
                .withEnabled(dictionaryInfo.getEnabled())
                .withValue(dictionaryInfo.getValue())
                .withParent(entity)
                .build();
    }

    /**
     * entity 转为 info
     *
     * @param entity
     * @return
     */
    private DictionaryInfo entityToInfo(DictionaryEntity entity) {
        // 通过递归方式判断有没有父结点
        DictionaryInfo parent = null;
        if (entity.getParent() != null) {
            parent = entityToInfo(entity.getParent());
        }
        return DictionaryInfo.DictionaryInfoBuilder
                .aDictionaryInfo()
                .withObjId(entity.getObjId())
                .withCreaterId(entity.getCreaterId())
                .withEnabled(entity.getEnabled())
                .withCreaterName(entity.getCreaterName())
                .withModifierId(entity.getModifierId())
                .withModifierName(entity.getModifierName())
                .withName(entity.getName())
                .withCode(entity.getCode())
                .withSeq(entity.getSeq())
                .withValue(entity.getValue())
                .withParent(parent)
                .build();
    }
}
