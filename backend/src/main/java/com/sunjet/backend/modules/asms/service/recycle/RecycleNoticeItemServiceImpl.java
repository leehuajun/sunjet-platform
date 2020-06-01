package com.sunjet.backend.modules.asms.service.recycle;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeEntity;
import com.sunjet.backend.modules.asms.entity.recycle.RecycleNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.recycle.view.RecycleNoticePendingView;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticePendingViewRepository;
import com.sunjet.backend.modules.asms.repository.recycle.RecycleNoticeRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 * 故障件返回通知单子行
 */
@Transactional
@Service("recycleNoticeItemService")
public class RecycleNoticeItemServiceImpl implements RecycleNoticeItemService {
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;

    @Autowired
    private RecycleNoticePendingViewRepository recycleNoticePendingViewRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private RecycleNoticeRepository recycleNoticeRepository;

    /**
     * 通过info保存一个entity
     *
     * @param recycleNoticeItemInfo
     * @return info
     */
    @Override
    public RecycleNoticeItemInfo save(RecycleNoticeItemInfo recycleNoticeItemInfo) {
        try {
            RecycleNoticeItemEntity entity = recycleNoticeItemRepository.save(BeanUtils.copyPropertys(recycleNoticeItemInfo, new RecycleNoticeItemEntity()));
            return BeanUtils.copyPropertys(entity, recycleNoticeItemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId查找entity
     *
     * @param objId
     * @return RecycleNoticeItemInfo
     */
    @Override
    public RecycleNoticeItemInfo findOne(String objId) {
        try {
            RecycleNoticeItemEntity entity = recycleNoticeItemRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new RecycleNoticeItemInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info 删除entity
     *
     * @param recycleNoticeItemInfo
     * @return
     */
    @Override
    public boolean delete(RecycleNoticeItemInfo recycleNoticeItemInfo) {
        try {
            recycleNoticeItemRepository.delete(recycleNoticeItemInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId删除
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            recycleNoticeItemRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过返回通知单objId 查找返回通知列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<RecycleNoticeItemInfo> findByNoticeId(String objId) {
        try {
            List<RecycleNoticeItemEntity> recycleNoticeItemEntityList = recycleNoticeItemRepository.findByRecycleNoticeId(objId);
            List<RecycleNoticeItemInfo> recycleNoticeItemInfos = new ArrayList<>();
            for (RecycleNoticeItemEntity recycleNoticeItemEntity : recycleNoticeItemEntityList) {
                recycleNoticeItemInfos.add(BeanUtils.copyPropertys(recycleNoticeItemEntity, new RecycleNoticeItemInfo()));
            }
            return recycleNoticeItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId删除与返回通知单子行相关联数据信息
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByRecycleNoticeId(String objId) {
        try {
            recycleNoticeItemRepository.deleteByRecycleNoticeId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 待返回清单 分页查询列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<RecycleNoticePendingView> getPageList(PageParam<RecycleNoticePendingItem> pageParam) {
        //1.查询条件
        RecycleNoticePendingItem recycleNoticePendingItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RecycleNoticePendingView> specification = null;
        if (recycleNoticePendingItem != null) {
            specification = Specifications.<RecycleNoticePendingView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .gt("currentAmount", 0)
                    .like(StringUtils.isNotBlank(recycleNoticePendingItem.getDealerCode()), "dealerCode", "%" + recycleNoticePendingItem.getDealerCode() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticePendingItem.getDealerCode()), "dealerName", "%" + recycleNoticePendingItem.getDealerName() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticePendingItem.getDocNo()), "docNo", "%" + recycleNoticePendingItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticePendingItem.getSrcDocNo()), "srcDocNo", "%" + recycleNoticePendingItem.getSrcDocNo() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticePendingItem.getPartCode()), "partCode", "%" + recycleNoticePendingItem.getPartCode() + "%")
                    .like(StringUtils.isNotBlank(recycleNoticePendingItem.getPartName()), "partName", "%" + recycleNoticePendingItem.getPartName() + "%")
                    .between("createdTime", new Range<Date>(recycleNoticePendingItem.getStartDate(), DateHelper.getEndDate(recycleNoticePendingItem.getEndDate())))
                    .in((recycleNoticePendingItem.getObjIds() != null && recycleNoticePendingItem.getObjIds().size() > 0), "objId", recycleNoticePendingItem.getObjIds())
                    .build();
        }

        //3.执行查询
        Page<RecycleNoticePendingView> pages = recycleNoticePendingViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<WarrantyMaintenanceInfo> rows = new ArrayList<>();
        //for (WarrantyMaintenanceEntity entity : pages.getContent()) {
        //    WarrantyMaintenanceInfo info = entityToInfo(entity);
        //    rows.add(info);
        //}


        //5.组装分页信息及集合信息
        //PageResult<DealerInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 根据配件编号或配件名称搜索配件需求列表
     *
     * @param key
     * @param declerCode
     * @return
     */
    @Override
    public List<RecycleNoticePendingView> findByReturnOrParts(String key, String declerCode) {
        try {
            List<RecycleNoticePendingView> entityList = recycleNoticePendingViewRepository.findByReturnOrParts("%" + key.trim() + "%", declerCode);
            return entityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过vin 查询故障返回通知明细objid集合
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllRecycleItemsObjIdByVin(String vin) {
        //故障件返回通知单objid集合;
        List<String> objIds = new ArrayList<>();
        //故障件发回单通知单
        List<RecycleNoticeEntity> recycleNoticeEntities = new ArrayList<>();

        try {
            VehicleEntity vehicleEntity = vehicleRepository.findOneByVin(vin);
            if (vehicleEntity != null) {
                //来源单据id
                List<String> srcDocIds = new ArrayList<>();
                //根据车辆id查询三包单
                List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleObjId(vehicleEntity.getObjId());
                if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                    for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                        srcDocIds.add(warrantyMaintenanceEntity.getObjId());
                    }
                }
                //根据车辆id查活动分配单
                //List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleEntity.getObjId());
                //if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                //    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                //        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                //    }
                //}
                if (srcDocIds.size() > 0) {
                    recycleNoticeEntities = recycleNoticeRepository.findAllBySrcDocIds(srcDocIds);
                }
                if (recycleNoticeEntities.size() > 0) {
                    List<String> recycleNoticeObjIds = new ArrayList<>();
                    for (RecycleNoticeEntity recycleNoticeEntity : recycleNoticeEntities) {
                        recycleNoticeObjIds.add(recycleNoticeEntity.getObjId());
                    }
                    if (recycleNoticeObjIds.size() > 0) {
                        List<RecycleNoticeItemEntity> recycleNoticeItemEntities = recycleNoticeItemRepository.findAllByRecycleNoticeObjIds(recycleNoticeObjIds);
                        if (recycleNoticeItemEntities != null && recycleNoticeItemEntities.size() > 0) {
                            for (RecycleNoticeItemEntity recycleNoticeItemEntity : recycleNoticeItemEntities) {
                                objIds.add(recycleNoticeItemEntity.getObjId());
                            }
                        }
                    }
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    ///**
    // * 根据配件编号或名字关键字搜索故障件配件信息
    // * @param key
    // * @return
    // */
    //@Override
    //public List<RecycleNoticeItemInfo> findCanReturnParts(String key) {
    //    try {
    //        List<RecycleNoticeItemEntity> entityList = recycleNoticeItemRepository.findCanReturnParts("%" + key + "%");
    //        List<RecycleNoticeItemInfo> recycleNoticeItemInfos = new ArrayList<>();
    //        for (RecycleNoticeItemEntity recycleNoticeItemEntity : entityList) {
    //            recycleNoticeItemInfos.add(BeanUtils.copyPropertys(recycleNoticeItemEntity, new RecycleNoticeItemInfo()));
    //        }
    //        return recycleNoticeItemInfos;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return null;
    //    }
    //}


}
