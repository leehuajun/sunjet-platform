package com.sunjet.backend.modules.asms.service.supply;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.SupplyWaitingItemEntity;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyAllocationView;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyNoticeView;
import com.sunjet.backend.modules.asms.entity.supply.view.SupplyWaitingItemView;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.supply.*;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.supply.SupplyAllocationItem;
import com.sunjet.dto.asms.supply.SupplyNoticeItem;
import com.sunjet.dto.asms.supply.SupplyWaitingItemItem;
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
 * Created by zyh on 2016/11/21.
 * 待发货清单
 */

@Transactional
@Service("supplyWaitingItemService")
public class SupplyWaitingItemServiceImpl implements SupplyWaitingItemService {
    @Autowired
    private SupplyWaitingItemRepository supplyWaitingItemRepository;//待发货清单 dao
    @Autowired
    private SupplyWaitingItemViewRepository supplyWaitingItemViewRepository;//待发货清单 view
    @Autowired
    private SupplyAllocationViewRepository supplyAllocationViewRepository;//调拨分配

    @Autowired
    private SupplyNoticeViewRepository supplyNoticeViewRepository;//调拨通知
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;
    @Autowired
    private SupplyNoticeRepository supplyNoticeRepository;


    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<SupplyWaitingItemItem> getPageList(PageParam<SupplyWaitingItemItem> pageParam) {
        //1.查询条件
        SupplyWaitingItemItem supplyWaitingItemItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SupplyWaitingItemView> specification = null;

        if (supplyWaitingItemItem != null) {
            specification = Specifications.<SupplyWaitingItemView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .gt("surplusAmount", 0)
                    .like(StringUtils.isNotBlank(supplyWaitingItemItem.getPartCode()), "partCode", "%" + supplyWaitingItemItem.getPartCode() + "%")
                    .eq(StringUtils.isNotBlank(supplyWaitingItemItem.getDealerCode()), "dealerCode", supplyWaitingItemItem.getDealerCode())
                    .eq(StringUtils.isNotBlank(supplyWaitingItemItem.getDealerName()), "dealerName", supplyWaitingItemItem.getDealerName())
                    .eq(StringUtils.isNotBlank(supplyWaitingItemItem.getAgencyName()), "agencyName", supplyWaitingItemItem.getAgencyName())
                    .eq(StringUtils.isNotBlank(supplyWaitingItemItem.getServiceManager()), "serviceManager", supplyWaitingItemItem.getServiceManager())
                    .between((supplyWaitingItemItem.getStartDate() != null && supplyWaitingItemItem.getEndDate() != null), "createdTime", new Range<Date>(supplyWaitingItemItem.getStartDate(), DateHelper.getEndDate(supplyWaitingItemItem.getEndDate())))
                    .like(StringUtils.isNotBlank(supplyWaitingItemItem.getSrcDocNo()), "srcDocNo", "%" + supplyWaitingItemItem.getSrcDocNo() + "%")
                    .like(StringUtils.isNotBlank(supplyWaitingItemItem.getDocNo()), "docNo", "%" + supplyWaitingItemItem.getDocNo() + "%")
                    //.like(StringUtils.isNotBlank(supplyWaitingItemItem.getSrcDocNo()),"srcDocNo","%"+supplyWaitingItemItem.getSrcDocNo()+"%")
                    //.in((supplyWaitingItemItem.getObjIds() != null && supplyWaitingItemItem.getObjIds().size() > 0), "objId", supplyWaitingItemItem.getObjIds())
                    .build();
        }

        //3.执行查询
        Page<SupplyWaitingItemView> pages = supplyWaitingItemViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<SupplyWaitingItemItem> rows = new ArrayList<>();

        //获取调拨分配信息
        HashMap<String, SupplyAllocationItem> map = getSupplyAllocationItemMap(pages.getContent());

        for (SupplyWaitingItemView view : pages.getContent()) {

            SupplyWaitingItemItem item = new SupplyWaitingItemItem();

            item = BeanUtils.copyPropertys(view, item);

            //调拨分配信息
            item.setSupplyNoticeItem(map.get(view.getSupplyNoticeItemId()));

            rows.add(item);
        }

        //5.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 保存
     *
     * @param supplyWaitingItemItem
     * @return
     */
    @Override
    public SupplyWaitingItemItem save(SupplyWaitingItemItem supplyWaitingItemItem) {
        try {
            SupplyWaitingItemEntity entity = supplyWaitingItemRepository.save(BeanUtils.copyPropertys(supplyWaitingItemItem, new SupplyWaitingItemEntity()));
            supplyWaitingItemItem = BeanUtils.copyPropertys(entity, supplyWaitingItemItem);
            return supplyWaitingItemItem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId查找待发货清单
     *
     * @param objId
     * @return
     */
    @Override
    public SupplyWaitingItemItem findSupplyWaitingItemById(String objId) {
        try {
            SupplyWaitingItemEntity entity = supplyWaitingItemRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new SupplyWaitingItemItem());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除待返清单
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean delete(String objId) {
        try {
            supplyWaitingItemRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取调拨分配信息
     *
     * @return
     */
    private HashMap<String, SupplyAllocationItem> getSupplyAllocationItemMap(List<SupplyWaitingItemView> rows) {
        HashMap<String, SupplyAllocationItem> map = null;
        if (rows != null && rows.size() > 0) {
            //去重复
            Set set = new HashSet();
            ArrayList<String> ids = new ArrayList<>();
            for (SupplyWaitingItemView supplyWaitingItemView : rows) {
                if (StringUtils.isNotBlank(supplyWaitingItemView.getSupplyNoticeItemId()) && set.add(supplyWaitingItemView.getSupplyNoticeItemId())) {
                    ids.add(supplyWaitingItemView.getSupplyNoticeItemId());
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
     * 通过SrcDocNo查询所有调拨待发货单objid
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<String> findAllObjIdsBySrcDocNo(String srcDocNo) {
        //待发货objId
        List<String> objIds = new ArrayList<>();
        List<SupplyWaitingItemEntity> supplyWaitingItemEntities = new ArrayList<>();
        try {
            List<SupplyNoticeItemEntity> supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocNo(srcDocNo);
            if (supplyNoticeItemEntities != null && supplyNoticeItemEntities.size() > 0) {
                List<String> supplyNoticeObjIds = new ArrayList<>();
                for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                    supplyNoticeObjIds.add(supplyNoticeItemEntity.getObjId());
                }
                if (supplyNoticeObjIds.size() > 0) {
                    supplyWaitingItemEntities = supplyWaitingItemRepository.findAllBySupplyNoticeObjIds(supplyNoticeObjIds);
                }
                if (supplyWaitingItemEntities.size() > 0) {
                    for (SupplyWaitingItemEntity supplyWaitingItemEntity : supplyWaitingItemEntities) {
                        objIds.add(supplyWaitingItemEntity.getObjId());
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
     * 通过vin查询所有调拨待发货单objid
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllObjIdsByVin(String vin) {
        //待发货id
        List<String> objIds = new ArrayList<>();
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
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
                List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleEntity.getObjId());
                if (activityVehicleEntities != null && activityVehicleEntities.size() > 0) {
                    for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntities) {
                        srcDocIds.add(activityVehicleEntity.getActivityDistributionId());
                    }
                }
                if (srcDocIds.size() > 0) {
                    supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocIds(srcDocIds);
                }
                if (supplyNoticeItemEntities.size() > 0) {
                    List<String> supplyNoticeItemIds = new ArrayList<>();
                    for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                        supplyNoticeItemIds.add(supplyNoticeItemEntity.getObjId());
                    }
                    List<SupplyWaitingItemEntity> supplyWaitingItemEntities = supplyWaitingItemRepository.findAllBySupplyNoticeObjIds(supplyNoticeItemIds);
                    for (SupplyWaitingItemEntity supplyWaitingItemEntity : supplyWaitingItemEntities) {
                        objIds.add(supplyWaitingItemEntity.getObjId());
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
     * 通过vin查询所有调拨待发货单objid
     *
     * @param supplyNotcieDocNo
     * @return
     */
    @Override
    public List<String> findAllObjIdsBySupplyNoticeDocNo(String supplyNotcieDocNo) {
        //待发货id
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
                    List<SupplyWaitingItemEntity> supplyWaitingItemEntities = supplyWaitingItemRepository.findAllBySupplyNoticeObjIds(supplyNoticeItemObjIds);
                    if (supplyWaitingItemEntities != null && supplyWaitingItemEntities.size() > 0) {
                        for (SupplyWaitingItemEntity supplyWaitingItemEntity : supplyWaitingItemEntities) {
                            objIds.add(supplyWaitingItemEntity.getObjId());
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
     * 获取合作商待发货配件
     *
     * @param agencyCode
     * @param partName
     * @return
     */
    @Override
    public List<SupplyWaitingItemItem> findAllPartByAgency(String agencyCode, String dealerCode, String partName) {
        List<SupplyWaitingItemEntity> supplyWaitingItemEntities = supplyWaitingItemRepository.findAllPartByAgency(agencyCode, dealerCode, "%" + partName + "%");
        List<SupplyWaitingItemItem> list = new ArrayList<>();
        for (SupplyWaitingItemEntity supplyWaitingItemEntity : supplyWaitingItemEntities) {
            list.add(BeanUtils.copyPropertys(supplyWaitingItemEntity, new SupplyWaitingItemItem()));
        }

        return list;
    }


    /**
     * 创建调拨供货前获取可创建配件
     *
     * @param agencyCode
     * @return
     */
    @Override
    public List<SupplyWaitingItemItem> findAllByAgencyCode(String agencyCode) {
        List<SupplyWaitingItemEntity> supplyWaitingItemEntities = supplyWaitingItemRepository.findAllByAgencyCode(agencyCode);
        List<SupplyWaitingItemItem> list = new ArrayList<>();
        for (SupplyWaitingItemEntity supplyWaitingItemEntity : supplyWaitingItemEntities) {
            list.add(BeanUtils.copyPropertys(supplyWaitingItemEntity, new SupplyWaitingItemItem()));
        }

        return list;
    }


}
