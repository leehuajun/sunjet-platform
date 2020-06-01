package com.sunjet.backend.modules.asms.service.activity;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityVehicleEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityDistributionView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.modules.asms.repository.activity.ActivityDistributionRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityDistributionViewRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityVehicleViewRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.activity.ActivityDistributionInfo;
import com.sunjet.dto.asms.activity.ActivityDistributionItem;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.JsonHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/11/5.
 * 活动发布单
 */
@Transactional
@Service("activityDistributionService")
public class ActivityDistributionServiceImpl implements ActivityDistributionService {
    @Autowired
    private ActivityDistributionRepository activityDistributionRepository;
    @Autowired
    private ActivityDistributionViewRepository activityDistributionViewRepository;

    @Autowired
    private ActivityVehicleViewRepository activityVehicleViewRepository;

    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private ProcessService processService;
    @Autowired
    private ActivityVehicleService activityVehicleService;
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;
    @Autowired
    private ActivityNoticeService activityNoticeService;


    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<ActivityDistributionView> getPageList(PageParam<ActivityDistributionItem> pageParam) {

        //1.查询条件
        ActivityDistributionItem activityDistributionItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ActivityDistributionView> specification = null;
        if (activityDistributionItem != null) {
            specification = Specifications.<ActivityDistributionView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(activityDistributionItem.getDocNo()), "docNo", activityDistributionItem.getDocNo())
                    .like(StringUtils.isNotBlank(activityDistributionItem.getDealerCode()), "dealerCode", "%" + activityDistributionItem.getDealerCode() + "%")
                    .like(StringUtils.isNotBlank(activityDistributionItem.getDealerName()), "dealerName", "%" + activityDistributionItem.getDealerName() + "%")
                    .eq(StringUtils.isNotBlank(activityDistributionItem.getServiceManager()), "serviceManager", activityDistributionItem.getServiceManager())
                    .eq(!activityDistributionItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", activityDistributionItem.getStatus())//表单状态
                    .ge(activityDistributionItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .like(StringUtils.isNotBlank(activityDistributionItem.getActivityNoticeDocNo()), "activityNoticeDocNo", "%" + activityDistributionItem.getActivityNoticeDocNo() + "%")
                    .between((activityDistributionItem.getStartDate() != null && activityDistributionItem.getEndDate() != null), "createdTime", new Range<Date>(activityDistributionItem.getStartDate(), activityDistributionItem.getEndDate()))
                    .in((activityDistributionItem.getActivityNoticeObjIds() != null && activityDistributionItem.getActivityNoticeObjIds().size() > 0), "activityNoticeId", activityDistributionItem.getActivityNoticeObjIds())
                    .build();


        }

        //3.执行查询
        Page<ActivityDistributionView> pages = activityDistributionViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<ActivityDistributionInfo> findAll() {
        try {
            List<ActivityDistributionEntity> list = this.activityDistributionRepository.findAll();
            List<ActivityDistributionInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (ActivityDistributionEntity activityDistributionEntity : list) {
                    ActivityDistributionInfo info = new ActivityDistributionInfo();
                    infos.add(BeanUtils.copyPropertys(activityDistributionEntity, info));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存活动分配实体
     *
     * @param activityDistributionEntity
     * @return
     */
    @Override
    public ActivityDistributionEntity save(ActivityDistributionEntity activityDistributionEntity) {
        try {
            if (activityDistributionEntity != null && StringUtils.isBlank(activityDistributionEntity.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(ActivityDistributionEntity.class.getSimpleName());
                activityDistributionEntity.setDocNo(docNo);
            }
            return activityDistributionRepository.save(activityDistributionEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 activityDistributionInfo 对象
     *
     * @param activityDistributionInfo
     * @return
     */
    @Override
    public boolean delete(ActivityDistributionInfo activityDistributionInfo) {
        try {
            ActivityDistributionEntity entity = BeanUtils.copyPropertys(activityDistributionInfo, new ActivityDistributionEntity());
            activityDistributionRepository.delete(entity);
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
    public boolean deleteById(String objId) {
        try {
            List<ActivityVehicleEntity> activityVehicleEntities = activityVehicleRepository.findAllVehicleByActivityDistributionObjId(objId);
            for (ActivityVehicleEntity entity : activityVehicleEntities) {
                entity.setActivityDistributionId(null);
                entity.setDistribute(false);
                activityVehicleRepository.save(entity);
            }
            activityDistributionRepository.delete(objId);
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
    public ActivityDistributionEntity findOneById(String objId) {
        try {
            return activityDistributionRepository.findOne(objId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找状态已关闭的相关服务站单据
     *
     * @param keyword    服务站搜索关键词
     * @param dealerCode 服务站编码
     * @return
     */
    @Override
    public List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(String keyword, String dealerCode) {
        List<ActivityDistributionEntity> activityDistributionEntityList = activityDistributionRepository.findAllByStatusAndKeywordAndDealerCode("%" + keyword + "%", dealerCode);
        return activityDistributionEntityList;
    }


    /**
     * 启动流程
     *
     * @param variables
     * @return
     */
    @Override
    public Map<String, String> startProcess(Map<String, Object> variables) {
        //提交流程返回信息
        Map<String, String> message = new HashMap<>();
        try {
            ActivityDistributionInfo activityDistributionInfo = JsonHelper.map2Bean(variables.get("entity"), ActivityDistributionInfo.class);
            //校验单据状态
            if (!activityDistributionInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ActivityDistributionEntity entity = BeanUtils.copyPropertys(activityDistributionInfo, new ActivityDistributionEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    //变更状态
                    ActivityDistributionEntity activityDistributionEntity = activityDistributionRepository.findOne(activityDistributionInfo.getObjId());
                    activityDistributionEntity.setProcessInstanceId(processInstanceInfo.getId());
                    activityDistributionEntity.setStatus(DocStatus.CLOSED.getIndex());
                    activityDistributionRepository.save(activityDistributionEntity);
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                }


                message.put("result", "提示");
                message.put("message", "提交成功");
            } else {
                message.put("result", "提示");
                message.put("message", "提交失败");
            }

            return message;
        } catch (IOException e) {
            e.printStackTrace();
            message.put("result", "提示");
            message.put("message", "提交失败");
            return message;
        }

    }

    /**
     * 通过车辆id查询活动分配id
     *
     * @param vehicleObjIds
     * @return
     */
    @Override
    public List<String> findAllObjIdsByVehicleId(List<String> vehicleObjIds) {
        List<String> objIds = new ArrayList<>();
        List<ActivityVehicleEntity> activityVehicleEntityList = new ArrayList<>();
        try {
            if (vehicleObjIds.size() > 0) {
                activityVehicleEntityList = activityVehicleService.findAllByVehicleIds(vehicleObjIds);
            }
            if (activityVehicleEntityList != null && activityVehicleEntityList.size() > 0) {
                for (ActivityVehicleEntity activityVehicle : activityVehicleEntityList) {
                    if (StringUtils.isNotBlank(activityVehicle.getActivityDistributionId())) {
                        objIds.add(activityVehicle.getActivityDistributionId());
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
     * 根据活动分配ID,获取活动对应的车辆Items
     *
     * @param distributionId
     * @return
     */
    @Override
    public List<ActivityVehicleView> findActivityVehicleItemsDistributionId(String distributionId) {
        return activityVehicleViewRepository.findActivityVehicleItemsDistributionId(distributionId);
    }

    @Override
    public Boolean deleteActivityDistributionVehicleItem(String activityVehicleObjId) {
        try {
            ActivityVehicleEntity entity = activityVehicleRepository.findOne(activityVehicleObjId);
            entity.setActivityDistributionId(null);
            entity.setDistribute(false);
            activityVehicleRepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityDistributionRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return activityDistributionRepository;
    }
}
