package com.sunjet.backend.modules.asms.service.supply;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyAllocationView;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyAllocationViewRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeViewRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by Administrator on 2016/11/7.
 * 调拨通知子行
 */
@Transactional
@Service("supplyNoticeItemService")
public class SupplyNoticeItemServiceImpl implements SupplyNoticeItemService {
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;

    @Autowired
    private SupplyAllocationViewRepository supplyAllocationViewRepository;

    @Autowired
    private SupplyNoticeViewRepository supplyNoticeViewRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;

    /**
     * 保存 实体
     *
     * @param supplyNoticeItemInfo
     * @return
     */
    @Override
    public SupplyNoticeItemInfo save(SupplyNoticeItemInfo supplyNoticeItemInfo) {
        try {
            SupplyNoticeItemEntity entity = supplyNoticeItemRepository.save(BeanUtils.copyPropertys(supplyNoticeItemInfo, new SupplyNoticeItemEntity()));
            return BeanUtils.copyPropertys(entity, supplyNoticeItemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 supplyNoticeItemInfo 对象
     *
     * @param supplyNoticeItemInfo
     * @return
     */
    @Override
    public boolean delete(SupplyNoticeItemInfo supplyNoticeItemInfo) {
        try {
            SupplyNoticeItemEntity entity = BeanUtils.copyPropertys(supplyNoticeItemInfo, new SupplyNoticeItemEntity());
            supplyNoticeItemRepository.delete(entity);
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
            supplyNoticeItemRepository.delete(objId);
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
    public SupplyNoticeItemInfo findOne(String objId) {
        try {
            SupplyNoticeItemEntity supplyDisItemEntity = supplyNoticeItemRepository.findOne(objId);
            return BeanUtils.copyPropertys(supplyDisItemEntity, new SupplyNoticeItemInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分页
     *
     * @return
     */
    @Override
    public PageResult<SupplyAllocationItem> getPageList(PageParam<SupplyAllocationItem> pageParam) {
        //1.查询条件
        SupplyAllocationItem supplyAllocationItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SupplyAllocationView> specification = null;
        if (supplyAllocationItem != null) {
            specification = Specifications.<SupplyAllocationView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(supplyAllocationItem.getPartCode()), "partCode", "%" + supplyAllocationItem.getPartCode() + "%")
                    //可分配数量大于0
                    //.gt("surplusAmount", 0)
                    .eq(StringUtils.isNotBlank(supplyAllocationItem.getSupplyNoticeId()), "supplyNoticeId", supplyAllocationItem.getSupplyNoticeId())
                    .eq(StringUtils.isNotBlank(supplyAllocationItem.getAgencyName()), "agencyName", supplyAllocationItem.getAgencyName())
                    .like(StringUtils.isNotBlank(supplyAllocationItem.getDocNo()), "docNo", "%" + supplyAllocationItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(supplyAllocationItem.getSrcDocNo()), "srcDocNo", "%" + supplyAllocationItem.getSrcDocNo() + "%")
                    .eq(StringUtils.isNotBlank(supplyAllocationItem.getAllocatedStatus()), "allocatedStatus", supplyAllocationItem.getAllocatedStatus())
                    .between("createdTime", new Range<Date>(supplyAllocationItem.getStartDate(), DateHelper.getEndDate(supplyAllocationItem.getEndDate())))
                    //.in((supplyAllocationItem.getObjIds() != null && supplyAllocationItem.getObjIds().size() > 0), "objId", supplyAllocationItem.getObjIds())
                    .build();
        }

        //3.执行查询
        Page<SupplyAllocationView> pages = supplyAllocationViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<SupplyAllocationItem> rows = new ArrayList<>();
        //获取通知单信息
        HashMap<String, SupplyNoticeItem> map = getSupplyNoticeItemMap(pages.getContent());

        for (SupplyAllocationView view : pages.getContent()) {

            SupplyAllocationItem item = new SupplyAllocationItem();

            item = BeanUtils.copyPropertys(view, item);
            //绑定通知单
            item.setSupplyNotice(map.get(view.getSupplyNoticeId()));

            rows.add(item);
        }

        //5.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 通过车辆vin 查询调拨通知单objids
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllObjIdByVin(String vin) {
        List<String> objIds = new ArrayList<>();
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        try {
            VehicleEntity vehicle = vehicleRepository.findOneByVin(vin);
            if (vehicle != null) {
                //来源单据id
                List<String> srcDocIds = new ArrayList<>();
                //根据车辆id查询三包单
                List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleObjId(vehicle.getObjId());
                if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                    for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                        srcDocIds.add(warrantyMaintenanceEntity.getObjId());
                    }
                }
                //根据车辆id查活动分配单
                List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicle.getObjId());
                if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                    }
                }
                if (srcDocIds.size() > 0) {
                    supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocIds(srcDocIds);
                    if (supplyNoticeItemEntities != null && supplyNoticeItemEntities.size() > 0) {
                        for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                            objIds.add(supplyNoticeItemEntity.getObjId());
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

    //获取通知单信息
    private HashMap<String, SupplyNoticeItem> getSupplyNoticeItemMap(List<SupplyAllocationView> rows) {
        HashMap<String, SupplyNoticeItem> map = null;
        if (rows != null && rows.size() > 0) {
            //去重复
            Set set = new HashSet();
            ArrayList<String> ids = new ArrayList<>();
            for (SupplyAllocationView supplyAllocationView : rows) {
                if (StringUtils.isNotBlank(supplyAllocationView.getSupplyNoticeId()) && set.add(supplyAllocationView.getSupplyNoticeId())) {
                    ids.add(supplyAllocationView.getSupplyNoticeId());
                }
            }
            //组装数据
            map = new HashMap<>();
            List<SupplyNoticeView> list = supplyNoticeViewRepository.getSupplyNoticeListByIds(ids);
            for (SupplyNoticeView supplyNoticeView : list) {
                map.put(supplyNoticeView.getObjId(), BeanUtils.copyPropertys(supplyNoticeView, new SupplyNoticeItem()));
            }
        }
        return map;
    }
}
