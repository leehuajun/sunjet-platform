package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintainEntity;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintainRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.asm.WarrantyMaintainInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 维修项目
 * Created by lhj on 16/9/17.
 */
@Slf4j
@Transactional
@Service("warrantyMaintainService")
public class WarrantyMaintainServiceImpl implements WarrantyMaintainService {
    @Autowired
    private WarrantyMaintainRepository warrantyMaintainRepository;

    /**
     * 保存 实体
     *
     * @param warrantyMaintainEntity
     * @return
     */
    @Override
    public WarrantyMaintainEntity save(WarrantyMaintainEntity warrantyMaintainEntity) {
        try {
            WarrantyMaintainEntity entity = warrantyMaintainRepository.save(warrantyMaintainEntity);
            //WarrantyMaintainEntity entity = warrantyMaintainRepository.save(BeanUtils.copyPropertys(warrantyMaintainInfo, new WarrantyMaintainEntity()));
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 warrantyMaintainInfo 对象
     *
     * @param warrantyMaintainInfo
     * @return
     */
    @Override
    public boolean delete(WarrantyMaintainInfo warrantyMaintainInfo) {
        try {
            warrantyMaintainRepository.delete(warrantyMaintainInfo.getObjId());
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
            warrantyMaintainRepository.delete(objId);
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
    public WarrantyMaintainInfo findOne(String objId) {
        try {
            WarrantyMaintainEntity warrantyMaintainEntity = warrantyMaintainRepository.findOne(objId);
            return BeanUtils.copyPropertys(warrantyMaintainEntity, new WarrantyMaintainInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过三包objid查找维修项目
     *
     * @param objId
     * @return
     */
    @Override
    public List<WarrantyMaintainInfo> findAllByWarrantyMaintenanceObjId(String objId) {
        try {
            List<WarrantyMaintainEntity> warrantyMaintainEntities = warrantyMaintainRepository.findAllByWarrantyMaintenanceObjId(objId);
            List<WarrantyMaintainInfo> warrantyMaintainInfos = new ArrayList<>();
            for (WarrantyMaintainEntity entity : warrantyMaintainEntities) {
                warrantyMaintainInfos.add(BeanUtils.copyPropertys(entity, new WarrantyMaintainInfo()));
            }
            log.info("WarrantyMaintainServiceImpl:findAllByWarrantyMaintenanceObjId:success");
            return warrantyMaintainInfos;
        } catch (Exception e) {
            log.info("WarrantyMaintainServiceImpl:findAllByWarrantyMaintenanceObjId:error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存维修项目
     *
     * @param warrantyMaintainEntityList
     * @return
     */
    @Override
    public List<WarrantyMaintainEntity> saveList(List<WarrantyMaintainEntity> warrantyMaintainEntityList) {
        return warrantyMaintainRepository.save(warrantyMaintainEntityList);
    }


}
