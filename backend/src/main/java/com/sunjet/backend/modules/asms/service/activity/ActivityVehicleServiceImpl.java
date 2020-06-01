package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleViewRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.system.Jpa.Specifications;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.activity.ActivityVehicleInfo;
import com.sunjet.dto.asms.activity.ActivityVehicleItem;
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
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("activityVehicleService")
public class ActivityVehicleServiceImpl implements ActivityVehicleService {
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;
    @Autowired
    private ActivityVehicleViewRepository activityVehicleViewRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<ActivityVehicleView> getPageList(PageParam<ActivityVehicleItem> pageParam) {

        //1.查询条件
        ActivityVehicleItem activityVehicleItem = pageParam.getInfoWhere();
        if (activityVehicleItem.getActivityNoticeId() != null) {
            activityVehicleItem.setVehicleId(null);
        }
        //2.设置查询参数
        Specification<ActivityVehicleView> specification = null;
        if (activityVehicleItem != null) {
            specification = Specifications.<ActivityVehicleView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(activityVehicleItem.getVehicleId() != null, "vehicleId", activityVehicleItem.getVehicleId())
                    .eq(activityVehicleItem.getActivityNoticeId() != null, "activityNoticeId", activityVehicleItem.getActivityNoticeId())
                    .eq(activityVehicleItem.getActivityDistributionId() != null, "activityDistributionId", activityVehicleItem.getActivityDistributionId())
                    .build();


        }
        //3.执行查询
        Page<ActivityVehicleView> pages = activityVehicleViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<ActivityVehicleView> getactivityVehiclePageListScreenActivityDistributionIdIsNULL(PageParam<ActivityVehicleItem> pageParam) {

        //1.查询条件
        ActivityVehicleItem activityVehicleItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ActivityVehicleView> specification = null;
        if (activityVehicleItem != null) {
            specification = Specifications.<ActivityVehicleView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(activityVehicleItem.getActivityNoticeId() != null, "activityNoticeId", activityVehicleItem.getActivityNoticeId())
                    .like(activityVehicleItem.getVin() != null, "vin", "%" + activityVehicleItem.getVin() + "%")
                    .like(activityVehicleItem.getVsn() != null, "vsn", "%" + activityVehicleItem.getVsn() + "%")
                    .like(activityVehicleItem.getVehicleModel() != null, "vehicleModel", "%" + activityVehicleItem.getVehicleModel() + "%")
                    .like(activityVehicleItem.getSeller() != null, "seller", "%" + activityVehicleItem.getSeller() + "%")
                    .eq(activityVehicleItem.getActivityDistributionId() == null, "activityDistributionId", activityVehicleItem.getActivityDistributionId())
                    .build();


        }
        //3.执行查询
        Page<ActivityVehicleView> pages = activityVehicleViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }

    /**
     * 保存 实体
     *
     * @param activityVehicleEntity
     * @return
     */
    @Override
    public ActivityVehicleEntity save(ActivityVehicleEntity activityVehicleEntity) {
        try {
            ActivityVehicleEntity entity = activityVehicleRepository.save(activityVehicleEntity);
            //ActivityVehicleView activityVehicleView = findOneById(entity.getObjId());
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过传过来的对象查到一条数据，设置activityDistributionId
     *
     * @param activityVehicleInfoList
     * @return
     */
    @Override
    public List<ActivityVehicleInfo> saveActivityDistributionId(List<ActivityVehicleInfo> activityVehicleInfoList) {
        try {

            List<ActivityVehicleEntity> entityList = new ArrayList<>();
            List<ActivityVehicleInfo> infoList = new ArrayList<>();
            for (ActivityVehicleInfo info : activityVehicleInfoList) {
                ActivityVehicleEntity entity = BeanUtils.copyPropertys(info, new ActivityVehicleEntity());
                entityList.add(entity);
            }
            //保存数据
            activityVehicleRepository.save(entityList);

            for (ActivityVehicleEntity entity : entityList) {
                infoList.add(BeanUtils.copyPropertys(entity, new ActivityVehicleInfo()));
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 activityVehicleInfo 对象
     *
     * @param activityVehicleInfo
     * @return
     */
    @Override
    public boolean delete(ActivityVehicleInfo activityVehicleInfo) {
        try {
            ActivityVehicleEntity entity = BeanUtils.copyPropertys(activityVehicleInfo, new ActivityVehicleEntity());
            activityVehicleRepository.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 如果是活动通知单的时候
     * 删除 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteById(String objId) {
        try {
            activityVehicleRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 如果是活动分配单的时候
     * 删除关联 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteRels(String objId) {
        try {
            ActivityVehicleEntity entity = activityVehicleRepository.findOne(objId);
            entity.setActivityDistributionId(null);
            entity.setDistribute(false);
            activityVehicleRepository.save(entity);
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
    public ActivityVehicleEntity findOne(String objId) {
        try {
            ActivityVehicleEntity entity = activityVehicleRepository.findOne(objId);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动通知单ID查活动车辆的数量
     *
     * @param activityNoticeId
     * @return
     */
    @Override
    public Integer findCountVehicleByActivityNoticeId(String activityNoticeId) {
        try {
            Integer VehicleSize = activityVehicleRepository.countVehicleSize(activityNoticeId);
            return VehicleSize;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动通知单的ID查当前活动单下的所有车辆
     *
     * @param objId
     * @return
     */
    @Override
    public List<ActivityVehicleInfo> findVehicleListByActivityNoticeId(String objId) {
        try {
            List<ActivityVehicleEntity> entityList = activityVehicleRepository.findVehicleListByActibityNoticeId(objId);
            List<ActivityVehicleInfo> infoList = new ArrayList<>();
            for (ActivityVehicleEntity entity : entityList) {
                infoList.add(BeanUtils.copyPropertys(entity, new ActivityVehicleInfo()));
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过活动分配单objId查询活动单车辆
     *
     * @param objId
     * @return
     */
    @Override
    public List<ActivityVehicleView> searchActivityVehicleByActivityDistributionId(String objId, String keyword) {
        //2.设置查询参数
        Specification<ActivityVehicleView> specification = null;
        if (StringUtils.isNotBlank(objId)) {
            specification = Specifications.<ActivityVehicleView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq("activityDistributionId", objId)
                    .eq("repair", false)
                    .like(StringUtils.isNotBlank(keyword), "vin", "%" + keyword + "%")
                    .build();
        }
        return activityVehicleViewRepository.findAll(specification);
    }

    /**
     * 通过objid查询活动车辆视图
     *
     * @param objId
     * @return
     */
    @Override
    public ActivityVehicleView findOneById(String objId) {
        return activityVehicleViewRepository.findOne(objId);
    }

    /**
     * 通过活动通知单ID和车辆ID删除一条活动车辆
     *
     * @param activityVehicleId
     * @return
     */
    @Override
    public boolean deleteActivityVehicleById(String activityVehicleId) {
        try {
//            String activityNoticeId = (String) map.get("activityNoticeId");
//            List<String> unSelectVehicleIdList = (List<String>) map.get("unSelectVehicleIdList");
            activityVehicleRepository.deleteActivityVehicleById(activityVehicleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 通过活动通知单ID删除该活动单下面的所有活动车辆
     *
     * @param activityNoticeId
     * @return
     */
    @Override
    public boolean deleteByActivityNoticeId(String activityNoticeId) {
        try {
            activityVehicleRepository.deleteByActivityNoticeId(activityNoticeId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过活动分配单的objID获取活动车辆
     *
     * @param objId
     * @return
     */
    @Override
    public List<ActivityVehicleView> searchActivityVehicleByActivityDistributionId(String objId) {
        Specification<ActivityVehicleView> specification = null;
        if (StringUtils.isNotBlank(objId)) {
            specification = Specifications.<ActivityVehicleView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq("activityDistributionId", objId)
                    .build();
        }
        List<ActivityVehicleView> activityVehicleViews = activityVehicleViewRepository.findAll(specification);
        return activityVehicleViews;
    }

    @Override
    public List<ActivityVehicleEntity> saveList(List<ActivityVehicleEntity> list) {
        try {
            return activityVehicleRepository.save(list);
//            List<ActivityVehicleEntity> entities = new ArrayList<>();
//            for(ActivityVehicleInfo info : infos){
//                entities.add(BeanUtils.copyPropertys(info,new ActivityVehicleEntity()));
//            }
//            entities = activityVehicleRepository.save(entities);
//
//            infos.clear();
//            for(ActivityVehicleEntity entity:entities){
//                infos.add(BeanUtils.copyPropertys(entities,new ActivityVehicleInfo()));
//            }
//            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过Vin查询活动车辆objid
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllObjIdByVin(String vin) {
        List<String> objIds = new ArrayList<>();
        try {
            List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByKeyword("%" + vin + "%");
            if (vehicleEntities != null && vehicleEntities.size() > 0) {
                List<String> vehiceleObjIds = new ArrayList<>();
                for (VehicleEntity vehicleEntity : vehicleEntities) {
                    vehiceleObjIds.add(vehicleEntity.getObjId());
                }
                if (vehiceleObjIds.size() > 0) {
                    List<ActivityVehicleEntity> activityVehicleEntityList = activityVehicleRepository.findAllByVehicelIds(vehiceleObjIds);
                    if (activityVehicleEntityList != null && activityVehicleEntityList.size() > 0) {
                        for (ActivityVehicleEntity activityVehicleEntity : activityVehicleEntityList) {
                            objIds.add(activityVehicleEntity.getObjId());
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
     * 通过车辆id查询活动车辆
     *
     * @param vehicleObjIds
     * @return
     */
    @Override
    public List<ActivityVehicleEntity> findAllByVehicleIds(List<String> vehicleObjIds) {
        return activityVehicleRepository.findAllByVehicelIds(vehicleObjIds);
    }

    /**
     * 通过车辆ID查所有活动车辆
     *
     * @param vehicleId
     * @return
     */
    @Override
    public List<ActivityVehicleEntity> findAllByVehicleId(String vehicleId) {
        List<ActivityVehicleEntity> activityVehicleEntities = new ArrayList<>();
        try {
            activityVehicleEntities = activityVehicleRepository.findAllActivityVehicleByVehicelId(vehicleId);
            return activityVehicleEntities;
        } catch (Exception e) {
            e.printStackTrace();
            return activityVehicleEntities;
        }
    }
}
