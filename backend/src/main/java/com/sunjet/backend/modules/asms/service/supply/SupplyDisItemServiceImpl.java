package com.sunjet.backend.modules.asms.service.supply;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyDisItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyWaitingItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyAllocationView;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyDisItemView;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.supply.*;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyDisItemInfo;
import com.sunjet.dto.asms.supply.SupplyDisItemItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by zyh on 2016/11/21.
 * 调拨分配
 */
@Transactional
@Service("supplyDisItemService")
public class SupplyDisItemServiceImpl implements SupplyDisItemService {
    @Autowired
    private SupplyDisItemRepository supplyDisItemRepository;//二次分配 dao

    @Autowired
    private SupplyDisItemViewRepository supplyDisItemViewRepository;//二次分配 view

    @Autowired
    private SupplyAllocationViewRepository supplyAllocationViewRepository;//调拨分配

    @Autowired
    private SupplyNoticeViewRepository supplyNoticeViewRepository;//调拨通知

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;

    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;
    @Autowired
    private SupplyNoticeRepository supplyNoticeRepository;


    /**
     * 保存 实体
     *
     * @param supplyDisItemInfo
     * @return
     */
    @Override
    public SupplyDisItemInfo save(SupplyDisItemInfo supplyDisItemInfo) {
        try {
            SupplyDisItemEntity entity = supplyDisItemRepository.save(BeanUtils.copyPropertys(supplyDisItemInfo, new SupplyDisItemEntity()));
            return BeanUtils.copyPropertys(entity, supplyDisItemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 supplyDisItemInfo 对象
     *
     * @param supplyDisItemInfo
     * @return
     */
    @Override
    public boolean delete(SupplyDisItemInfo supplyDisItemInfo) {
        try {
            this.delete(supplyDisItemInfo.getObjId());
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
            supplyDisItemRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<SupplyDisItemItem> getPageList(PageParam<SupplyDisItemItem> pageParam) {

        //1.查询条件
        SupplyDisItemItem supplyDisItemItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SupplyDisItemView> specification = null;

        if (supplyDisItemItem != null) {
            specification = Specifications.<SupplyDisItemView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(supplyDisItemItem.getPartCode()), "partCode", "%" + supplyDisItemItem.getPartCode() + "%")
                    //可分配数量大于0
                    .like(StringUtils.isNotBlank(supplyDisItemItem.getSrcDocNo()), "srcDocNo", "%" + supplyDisItemItem.getSrcDocNo() + "%")
                    .like(StringUtils.isNotBlank(supplyDisItemItem.getDocNo()), "docNo", "%" + supplyDisItemItem.getDocNo() + "%")
                    .gt("surplusAmount", 0)
                    .between("createdTime", new Range<Date>(supplyDisItemItem.getStartDate(), supplyDisItemItem.getEndDate()))
                    .build();
        }

        //3.执行查询
        Page<SupplyDisItemView> pages = supplyDisItemViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<SupplyDisItemItem> rows = new ArrayList<>();

        //获取调拨分配信息
        HashMap<String, SupplyAllocationItem> map = getSupplyAllocationItemMap(pages.getContent());

        for (SupplyDisItemView view : pages.getContent()) {

            SupplyDisItemItem item = new SupplyDisItemItem();

            item = BeanUtils.copyPropertys(view, item);

            //调拨分配信息
            item.setSupplyNoticeItem(map.get(view.getSupplyNoticeItemId()));

            rows.add(item);
        }

        //5.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 通过SrcDocNo查询所有二次分配objid
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<String> finAllObjIdBySrcDocNo(String srcDocNo) {
        List<String> objIds = new ArrayList<>();
        List<SupplyWaitingItemEntity> supplyWaitingItemEntities = new ArrayList<>();
        try {
            List<SupplyNoticeItemEntity> supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocNo(srcDocNo);
            if (supplyNoticeItemEntities != null && supplyNoticeItemEntities.size() > 0) {
                List<String> supplyNoticeItemObjId = new ArrayList<>();
                for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                    supplyNoticeItemObjId.add(supplyNoticeItemEntity.getObjId());
                }
                if (supplyNoticeItemObjId.size() > 0) {
                    List<SupplyDisItemEntity> supplyDisItemEntities = supplyDisItemRepository.finDAllBySupplyNotice(supplyNoticeItemObjId);
                    if (supplyDisItemEntities != null && supplyDisItemEntities.size() > 0) {
                        for (SupplyDisItemEntity supplyDisItemEntity : supplyDisItemEntities) {
                            objIds.add(supplyDisItemEntity.getObjId());

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

    /**
     * 通过vin查询所有二次分配objid
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> finAllObjIdByVin(String vin) {
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
                    List<String> supplyNoticeItemObjId = new ArrayList<>();
                    if (supplyNoticeItemEntities != null && supplyNoticeItemEntities.size() > 0) {
                        for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                            supplyNoticeItemObjId.add(supplyNoticeItemEntity.getObjId());
                        }
                    }
                    if (supplyNoticeItemObjId.size() > 0) {
                        List<SupplyDisItemEntity> supplyDisItemEntities = supplyDisItemRepository.finDAllBySupplyNotice(supplyNoticeItemObjId);
                        if (supplyDisItemEntities != null && supplyDisItemEntities.size() > 0) {
                            for (SupplyDisItemEntity supplyDisItemEntity : supplyDisItemEntities) {
                                objIds.add(supplyDisItemEntity.getObjId());

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

    /**
     * 通过supplyNotcieDocNo查询所有二次分配objid
     *
     * @param supplyNotcieDocNo
     * @return
     */
    @Override
    public List<String> finAllObjIdBySupplyNoticeDocNo(String supplyNotcieDocNo) {
        //二次分配Id
        List<String> objIds = new ArrayList<>();
        //调拨通知单子行
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        try {
            SupplyNoticeEntity supplyNoticeEntity = supplyNoticeRepository.findOneByDocNo("%" + supplyNotcieDocNo + "%");
            if (supplyNoticeEntity != null) {
                supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySupplyNoticeObjId(supplyNoticeEntity.getObjId());
            }
            //有调拨通知子行
            if (supplyNoticeItemEntities.size() > 0) {
                List<String> supplyNoticeItemObjIds = new ArrayList<>();
                for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                    supplyNoticeItemObjIds.add(supplyNoticeItemEntity.getObjId());
                }
                if (supplyNoticeItemObjIds.size() > 0) {
                    List<SupplyDisItemEntity> supplyDisItemEntities = supplyDisItemRepository.finDAllBySupplyNotice(supplyNoticeItemObjIds);
                    if (supplyDisItemEntities != null && supplyDisItemEntities.size() > 0) {
                        for (SupplyDisItemEntity supplyDisItemEntity : supplyDisItemEntities) {
                            objIds.add(supplyDisItemEntity.getObjId());

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

    /**
     * 获取调拨分配信息
     *
     * @return
     */
    private HashMap<String, SupplyAllocationItem> getSupplyAllocationItemMap(List<SupplyDisItemView> rows) {
        HashMap<String, SupplyAllocationItem> map = null;
        if (rows != null && rows.size() > 0) {
            //去重复
            Set set = new HashSet();
            ArrayList<String> ids = new ArrayList<>();
            for (SupplyDisItemView supplyDisItemView : rows) {
                if (StringUtils.isNotBlank(supplyDisItemView.getSupplyNoticeItemId()) && set.add(supplyDisItemView.getSupplyNoticeItemId())) {
                    ids.add(supplyDisItemView.getSupplyNoticeItemId());
                }
            }

            //组装数据
            map = new HashMap<>();
            List<SupplyAllocationView> list = supplyAllocationViewRepository.getSupplyAllocationByIds(ids);
            //调拨通知
            HashMap<String, SupplyNoticeItem> supplyNoticeItemHashMap = getSupplyNoticeItemMap(list);
            for (SupplyAllocationView supplyAllocationView : list) {
                //调拨分配
                SupplyAllocationItem supplyAllocationItem = BeanUtils.copyPropertys(supplyAllocationView, new SupplyAllocationItem());
                //绑定调拨通知
                supplyAllocationItem.setSupplyNotice(supplyNoticeItemHashMap.get(supplyAllocationView.getSupplyNoticeId()));

                map.put(supplyAllocationView.getObjId(), supplyAllocationItem);
            }
        }
        return map;
    }

    //获取通知单信息
    private HashMap<String, SupplyNoticeItem> getSupplyNoticeItemMap(List<SupplyAllocationView> rows) {
        HashMap<String, SupplyNoticeItem> map = null;
        if (rows != null && rows.size() > 0) {
            //去重复
            Set set = new HashSet();
            ArrayList<String> ids = new ArrayList<>();
            for (SupplyAllocationView supplyAllocationView : rows) {
                if (set.add(supplyAllocationView.getSupplyNoticeId())) {
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


    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public SupplyDisItemInfo findOne(String objId) {
        try {
            SupplyDisItemEntity supplyDisItemEntity = supplyDisItemRepository.findOne(objId);
            return BeanUtils.copyPropertys(supplyDisItemEntity, new SupplyDisItemInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
