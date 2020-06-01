package com.sunjet.backend.modules.asms.service.recycle;


import com.sunjet.backend.modules.asms.entity.recycle.RecycleItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleItemPartView;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleItemPartViewRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleItemRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 故障件返回单子行
 * <p>
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("recycleItemService")
public class RecycleItemServiceImpl implements RecycleItemService {
    @Autowired
    private RecycleItemRepository recycleItemRepository;
    @Autowired
    private RecycleItemPartViewRepository recycleItemPartViewRepository;

    /**
     * 通过info保存一个实体
     *
     * @param recycleItemInfo
     * @return
     */
    @Override
    public RecycleItemInfo save(RecycleItemInfo recycleItemInfo) {
        try {
            RecycleItemEntity entity = recycleItemRepository.save(BeanUtils.copyPropertys(recycleItemInfo, new RecycleItemEntity()));
            return BeanUtils.copyPropertys(entity, recycleItemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过objId查找一个info
     *
     * @param objId
     * @return RecycleItemInfo
     */
    @Override
    public RecycleItemEntity findOne(String objId) {
        try {
            RecycleItemEntity entity = recycleItemRepository.findOne(objId);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过info删除实体
     *
     * @param recycleItemInfo
     * @return 是否删除
     */
    @Override
    public boolean delete(RecycleItemInfo recycleItemInfo) {
        try {
            recycleItemRepository.delete(recycleItemInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过obji 删除一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            recycleItemRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过故障件返回单id 查找返回单子行列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<RecycleItemEntity> findByRecycle(String objId) {
        try {
            List<RecycleItemEntity> recycleItemEntityList = recycleItemRepository.findByRecycle(objId);
            //List<RecycleItemInfo> recycleItemInfos = new ArrayList<>();
            //for (RecycleItemEntity recycleItemEntity : recycleItemEntityList) {
            //
            //    RecycleItemInfo recycleItemInfo = BeanUtils.copyPropertys(recycleItemEntity, new RecycleItemInfo());
            //    recycleItemInfos.add(recycleItemInfo);
            //}
            return recycleItemEntityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId（通知单子行id）查找返回通知单子行列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<RecycleItemInfo> findAllByNoticeItemId(String objId) {
        try {
            List<RecycleItemEntity> recycleItemEntityList = recycleItemRepository.findAllByNoticeItemId(objId);
            List<RecycleItemInfo> recycleItemInfos = new ArrayList<>();
            for (RecycleItemEntity recycleItemEntity : recycleItemEntityList) {
                recycleItemInfos.add(BeanUtils.copyPropertys(recycleItemEntity, new RecycleItemInfo()));
            }
            return recycleItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过 recycle 查找故障件返回单配件需求列表
     *
     * @param recycle
     * @return
     */
    @Override
    public List<RecycleItemPartView> findByRecyclePartList(String recycle) {
        try {
            List<RecycleItemPartView> recycleItemPartViews = recycleItemPartViewRepository.findByRecyclePartList(recycle);

            return recycleItemPartViews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过父表objId删除子表
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByRecycleObjId(String objId) {

        try {
            recycleItemRepository.deleteByRecycleObjId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过来源单号查询所有故障件明细
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<RecycleItemEntity> findAllRecycleItemBySrcDocNo(String srcDocNo) {

        return recycleItemRepository.findAllRecycleItemBySrcDocNo(srcDocNo);
    }

    /**
     * 通过来源单Id查询所有故障件明细
     *
     * @param srcDocNos
     * @return
     */
    @Override
    public List<RecycleItemEntity> findAllRecycleItemBySrcDocNos(List<String> srcDocNos) {
        return recycleItemRepository.findAllRecycleItemBySrcDocNos(srcDocNos);
    }

    /**
     * info 转换为entity
     *
     * @param recycleItemInfo
     * @return
     */
    //private RecycleItemEntity infoToEntity(RecycleItemInfo recycleItemInfo) {
    //    return RecycleItemEntity.RecycleItemEntityBuilder
    //            .aRecycleItemEntity()
    //            .withCreaterId(recycleItemInfo.getCreaterId())
    //            .withCreaterName(recycleItemInfo.getCreaterName())
    //            .withLogisticsNum(recycleItemInfo.getLogisticsNum())
    //            .withModifierId(recycleItemInfo.getModifierId())
    //            .withRequestDate(recycleItemInfo.getRequestDate())
    //            .withModifierName(recycleItemInfo.getModifierName())
    //            .withSrcDocNo(recycleItemInfo.getSrcDocNo())
    //            .withObjId(recycleItemInfo.getObjId())
    //            .withSrcDocType(recycleItemInfo.getSrcDocType())
    //            .withEnabled(recycleItemInfo.getEnabled())
    //            //.withPart(recycleItemInfo)
    //            .withCreatedTime(recycleItemInfo.getCreatedTime())
    //            .withPartCode(recycleItemInfo.getPartCode())
    //            .withPartName(recycleItemInfo.getPartName())
    //            .withModifiedTime(recycleItemInfo.getModifiedTime())
    //            .withWarrantyTime(recycleItemInfo.getWarrantyTime())
    //            .withWarrantyMileage(recycleItemInfo.getWarrantyMileage())
    //            .withWaitAmount(recycleItemInfo.getWaitAmount())
    //            .withBackAmount(recycleItemInfo.getBackAmount())
    //            .withAcceptAmount(recycleItemInfo.getAcceptAmount())
    //            .withPattern(recycleItemInfo.getPattern())
    //            .withReason(recycleItemInfo.getReason())
    //            .withComment(recycleItemInfo.getComment())
    //            //.withRecycleEntity(recycleItemInfo)
    //            //.withRecycleNoticeItem(recycleItemInfo.)
    //            .build();
    //}


    /**
     * entity转换为info
     *
     * @param recycleItemEntity
     * @return
     */
    //private RecycleItemInfo entityToinfo(RecycleItemEntity recycleItemEntity) {
    //    return RecycleItemInfo.RecycleItemInfoBuilder
    //            .aRecycleItemInfo()
    //            .withCreaterId(recycleItemEntity.getCreaterId())
    //            .withCreaterName(recycleItemEntity.getCreaterName())
    //            .withLogisticsNum(recycleItemEntity.getLogisticsNum())
    //            .withModifierId(recycleItemEntity.getModifierId())
    //            .withRequestDate(recycleItemEntity.getRequestDate())
    //            .withModifierName(recycleItemEntity.getModifierName())
    //            .withSrcDocNo(recycleItemEntity.getSrcDocNo())
    //            .withObjId(recycleItemEntity.getObjId())
    //            .withSrcDocType(recycleItemEntity.getSrcDocType())
    //            .withEnabled(recycleItemEntity.getEnabled())
    //            //.withPart(recycleItemEntity)
    //            .withCreatedTime(recycleItemEntity.getCreatedTime())
    //            .withPartCode(recycleItemEntity.getPartCode())
    //            .withPartName(recycleItemEntity.getPartName())
    //            .withModifiedTime(recycleItemEntity.getModifiedTime())
    //            .withWarrantyTime(recycleItemEntity.getWarrantyTime())
    //            .withWarrantyMileage(recycleItemEntity.getWarrantyMileage())
    //            .withWaitAmount(recycleItemEntity.getWaitAmount())
    //            .withBackAmount(recycleItemEntity.getBackAmount())
    //            .withAcceptAmount(recycleItemEntity.getAcceptAmount())
    //            .withPattern(recycleItemEntity.getPattern())
    //            .withReason(recycleItemEntity.getReason())
    //            .withComment(recycleItemEntity.getComment())
    //            //.withRecycleEntity(recycleItemInfo)
    //            //.withRecycleNoticeItem(recycleItemInfo.)
    //            .build();
    //
    //}

}
