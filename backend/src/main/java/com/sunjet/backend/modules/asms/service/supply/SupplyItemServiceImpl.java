package com.sunjet.backend.modules.asms.service.supply;


import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyItemEntity;
import com.sunjet.backend.modules.asms.repository.basic.PartRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyItemRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 调拨供货子行
 */
@Transactional
@Service("supplyItemService")
public class SupplyItemServiceImpl implements SupplyItemService {
    @Autowired
    private SupplyItemRepository supplyItemRepository;
    @Autowired
    private PartRepository partRepository;


    //@Override
    //public List<SupplyItemEntity> findSupplyItemsByDocID(String docid) {
    //    return supplyItemRepository.findSupplyItemsByDocID(docid);
    //}

    /**
     * 保存 实体
     *
     * @param supplyItemInfo
     * @return
     */
    @Override
    public SupplyItemInfo save(SupplyItemInfo supplyItemInfo) {
        try {
            SupplyItemEntity entity = supplyItemRepository.save(BeanUtils.copyPropertys(supplyItemInfo, new SupplyItemEntity()));
            return BeanUtils.copyPropertys(entity, supplyItemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 supplyItemInfo 对象
     *
     * @param supplyItemInfo
     * @return
     */
    @Override
    public boolean delete(SupplyItemInfo supplyItemInfo) {
        try {
            SupplyItemEntity entity = BeanUtils.copyPropertys(supplyItemInfo, new SupplyItemEntity());
            supplyItemRepository.delete(entity);
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
            supplyItemRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据供货单id查供货单子行
     *
     * @param supplyId
     * @return
     */
    @Override
    public List<SupplyItemInfo> findBySupplyId(String supplyId) {
        try {
            List<SupplyItemEntity> supplyItemEntities = supplyItemRepository.findBySupplyId(supplyId);
            List<SupplyItemInfo> supplyItemInfos = new ArrayList<>();
            for (SupplyItemEntity recycleItemEntity : supplyItemEntities) {
                SupplyItemInfo supplyItemInfo = BeanUtils.copyPropertys(recycleItemEntity, new SupplyItemInfo());
                if (StringUtils.isNotBlank(recycleItemEntity.getPartId())) {
                    PartEntity partEntity = partRepository.findOne(recycleItemEntity.getPartId());
                    supplyItemInfo.setUnit(partEntity.getUnit());
                    supplyItemInfo.setWarrantyMileage(partEntity.getWarrantyMileage());
                    supplyItemInfo.setWarrantyTime(partEntity.getWarrantyTime());
                }
                supplyItemInfos.add(supplyItemInfo);

            }
            return supplyItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除调拨供货行
     *
     * @param noticeItemId
     * @return
     */
    @Override
    public boolean deleteBySupplyNoticeItems(String noticeItemId) {
        try {
            supplyItemRepository.deleteBySupplyNoticeItems(noticeItemId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过调拨供货单objId删除
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean deleteBySupplyObjId(String objId) {
        try {
            supplyItemRepository.deleteBySupplyObjId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过调拨单明细objid 获取供货单明细
     *
     * @param objId
     * @return
     */
    @Override
    public List<SupplyItemInfo> findAllByNoticeItemId(String objId) {
        List<SupplyItemInfo> supplyItemInfoList = new ArrayList<>();
        try {

            List<SupplyItemEntity> supplyItemList = supplyItemRepository.findBySupplyNoticeItemId(objId);
            for (SupplyItemEntity supplyItemEntity : supplyItemList) {
                supplyItemInfoList.add(BeanUtils.copyPropertys(supplyItemEntity, new SupplyItemInfo()));
            }
            return supplyItemInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return supplyItemInfoList;
        }
    }

    /**
     * 通过noticeItemIds查询调拨供货单子行明细
     *
     * @param objIds
     * @return
     */
    @Override
    public List<SupplyItemInfo> findAllByNoticeItemId(List<String> objIds) {
        List<SupplyItemEntity> supplyItemEntityList = supplyItemRepository.findAllByNoticeItemId(objIds);
        List<SupplyItemInfo> supplyItemInfoList = new ArrayList<>();
        for (SupplyItemEntity supplyItemEntity : supplyItemEntityList) {
            supplyItemInfoList.add(BeanUtils.copyPropertys(supplyItemEntity, new SupplyItemInfo()));
        }
        return supplyItemInfoList;
    }


    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public SupplyItemEntity findOne(String objId) {
        try {
            return supplyItemRepository.findOne(objId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
