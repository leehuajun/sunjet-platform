package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.settlement.FreightExpenseEntity;
import com.sunjet.backend.modules.asms.repository.settlement.FreightExpenseRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.settlement.FreightExpenseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("freightExpenseListService")
public class FreightExpenseListServiceImpl implements FreightExpenseListService {
    @Autowired
    private FreightExpenseRepository freightExpenseRepository;


    /**
     * 通过info查entity
     *
     * @param freightExpenseInfo
     * @return
     */
    @Override
    public FreightExpenseInfo save(FreightExpenseInfo freightExpenseInfo) {
        try {
            FreightExpenseEntity entity = freightExpenseRepository.save(BeanUtils.copyPropertys(freightExpenseInfo, new FreightExpenseEntity()));
            return BeanUtils.copyPropertys(entity, freightExpenseInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objI查实体
     *
     * @param objId
     * @return
     */
    @Override
    public FreightExpenseInfo findOne(String objId) {
        try {
            FreightExpenseEntity entity = freightExpenseRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new FreightExpenseInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info 删除实体
     *
     * @param freightExpenseInfo
     * @return
     */
    @Override
    public boolean delete(FreightExpenseInfo freightExpenseInfo) {
        try {
            freightExpenseRepository.delete(freightExpenseInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId 删除实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            freightExpenseRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过运费结算id查找运费结算子行列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<FreightExpenseInfo> findByFreightSettlementId(String objId) {
        try {
            List<FreightExpenseEntity> entityList = freightExpenseRepository.findByFreightSettlementId(objId);
            List<FreightExpenseInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (FreightExpenseEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new FreightExpenseInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除配件运费结算的同时把运费结算子行一同删除
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByFreightSettlementId(String objId) {
        try {
            freightExpenseRepository.deleteByFreightSettlementId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
