package com.sunjet.backend.modules.asms.service.activity;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.activity.ActivityDistributionEntity;
import com.sunjet.backend.modules.asms.entity.activity.ActivityNoticeEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityNoticeView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityVehicleView;
import com.sunjet.backend.modules.asms.entity.supply.SupplyNoticeItemEntity;
import com.sunjet.backend.modules.asms.repository.activity.*;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.modules.asms.service.supply.SupplyItemService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.activity.ActivityNoticeInfo;
import com.sunjet.dto.asms.activity.ActivityNoticeItem;
import com.sunjet.dto.asms.activity.ActivityPartInfo;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.supply.SupplyItemInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.DateHelper;
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
 * 服务活动通知单
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("activityNoticeService")
public class ActivityNoticeServiceImpl implements ActivityNoticeService {

    @Autowired
    private ActivityNoticeRepository activityNoticeRepository;
    @Autowired
    private ActivityNoticeViewRepository activityNoticeViewRepository;
    @Autowired
    private ActivityDistributionRepository activityDistributionRepository;
    @Autowired
    private SupplyNoticeRepository supplyNoticeRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;
    @Autowired
    private SupplyItemService supplyItemService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private ActivityPartService activityPartService;

    @Autowired
    private ActivityVehicleViewRepository activityVehicleViewRepository;
    @Autowired
    private ActivityPartViewRepository activityPartViewRepository;

    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;


    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<ActivityNoticeEntity> findAll() {
        try {
            List<ActivityNoticeEntity> list = this.activityNoticeRepository.findAll();
//            List<ActivityNoticeInfo> infos = null;
//            infos = new ArrayList<>();
//            if(list!=null&&list.size()>0) {
//                for (ActivityNoticeEntity activityNoticeEntity:list) {
//                    ActivityNoticeInfo info = new ActivityNoticeInfo();
//                    infos.add(BeanUtils.copyPropertys(activityNoticeEntity,info));
//                }
//            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param activityNoticeEntity
     * @return
     */
    @Override
    public ActivityNoticeEntity save(ActivityNoticeEntity activityNoticeEntity) {
        try {
            if (activityNoticeEntity != null && StringUtils.isBlank(activityNoticeEntity.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(ActivityNoticeEntity.class.getSimpleName());
                activityNoticeEntity.setDocNo(docNo);
            }
            ActivityNoticeEntity entity = activityNoticeRepository.save(activityNoticeEntity);
//            List<ActivityPartInfo> partInfoList = saveActivitPart(entity.getObjId(), activityNoticeInfo.getActivityPartInfos());
//            ActivityNoticeInfo activityNotice = BeanUtils.copyPropertys(entity, activityNoticeInfo);
//            activityNotice.setActivityPartInfos(partInfoList);

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关联配件
     *
     * @param activityNoticeId
     * @param activityPartInfos
     */
    private List<ActivityPartInfo> saveActivitPart(String activityNoticeId, List<ActivityPartInfo> activityPartInfos) {
        List<ActivityPartInfo> activityPartInfoArrayList = new ArrayList<>();
        for (ActivityPartInfo activityPartInfo : activityPartInfos) {
            activityPartInfo.setActivityNoticeId(activityNoticeId);
            ActivityPartInfo partInfo = activityPartService.save(activityPartInfo);
            activityPartInfoArrayList.add(partInfo);
        }
        return activityPartInfoArrayList;
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<ActivityNoticeView> getPageList(PageParam<ActivityNoticeItem> pageParam) {

        //1.查询条件
        ActivityNoticeItem activityNoticeItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ActivityNoticeView> specification = null;
        if (activityNoticeItem != null) {
            specification = Specifications.<ActivityNoticeView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(activityNoticeItem.getSubmitter()), "submitter", activityNoticeItem.getSubmitter())
                    .like(StringUtils.isNotBlank(activityNoticeItem.getDocNo()), "docNo", "%" + activityNoticeItem.getDocNo() + "%")
                    .like(StringUtils.isNotBlank(activityNoticeItem.getTitle()), "title", activityNoticeItem.getTitle())
                    .eq(!activityNoticeItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", activityNoticeItem.getStatus())//表单状态
                    .ge(activityNoticeItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((activityNoticeItem.getStartDate() != null && activityNoticeItem.getEndDate() != null), "createdTime", new Range<Date>(activityNoticeItem.getStartDate(), DateHelper.getEndDate(activityNoticeItem.getEndDate())))
                    .build();


        }

        //3.执行查询
        Page<ActivityNoticeView> pages = activityNoticeViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
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
            activityNoticeRepository.delete(objId);
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
    public ActivityNoticeEntity findOneById(String objId) {
        try {
            ActivityNoticeEntity Entity = activityNoticeRepository.findOne(objId);
//            List<ActivityPartInfo> activityPartInfos = activityPartService.findPartByActivityNoticeId(objId);
//            ActivityNoticeInfo activityNoticeInfo = BeanUtils.copyPropertys(Entity, new ActivityNoticeInfo());
//            activityNoticeInfo.setActivityPartInfos(activityPartInfos);
            return Entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ActivityNoticeEntity> searchCloseActivityNotices(Map<String, Object> map) {
        try {
            String keyword = (String) map.get("keyWord");
            List<ActivityNoticeEntity> list = this.activityNoticeRepository.searchCloseActivityNotices("%" + keyword + "%");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            ActivityNoticeInfo activityNoticeInfo = JsonHelper.map2Bean(variables.get("entity"), ActivityNoticeInfo.class);
            if (!activityNoticeInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ActivityNoticeEntity entity = BeanUtils.copyPropertys(activityNoticeInfo, new ActivityNoticeEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    activityNoticeInfo.setProcessInstanceId(processInstanceInfo.getId());
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                }

                //变更状态
                activityNoticeInfo.setStatus(DocStatus.CLOSED.getIndex());
                activityNoticeRepository.save(BeanUtils.copyPropertys(activityNoticeInfo, new ActivityNoticeEntity()));
                //save(activityNoticeInfo);

                message.put("result", "提示");
                message.put("message", "提交成功");
            } else {
                message.put("result", "提示");
                message.put("message", "此单据已经提交");
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
     * 通过单据编号查询调拨通知单子行
     *
     * @param docNo
     * @return
     */
    @Override
    public List<SupplyItemInfo> findSupplyItemIdsByDocNo(String docNo) {
        //调拨供货单子行
        List<SupplyItemInfo> supplyItemInfoList = new ArrayList<>();
        //调拨通知子行
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = new ArrayList<>();
        try {
            ActivityNoticeEntity activityNoticeEntity = activityNoticeRepository.findoneByDocNo("%" + docNo + "%");
            if (activityNoticeEntity != null) {
                //获取活动分配单
                List<ActivityDistributionEntity> activityDistributionEntities = activityDistributionRepository.findAllByActivityNoticeObjId(activityNoticeEntity.getObjId());
                if (activityDistributionEntities != null && activityDistributionEntities.size() > 0) {
                    List<String> srcObjids = new ArrayList<>();//活动分配单id
                    for (ActivityDistributionEntity activityDistributionEntity : activityDistributionEntities) {
                        srcObjids.add(activityDistributionEntity.getObjId());
                    }
                    if (srcObjids.size() > 0) {
                        supplyNoticeItemEntities = supplyNoticeItemRepository.findAllBySrcDocIds(srcObjids);
                    }
                    //调拨通知子行不为0
                    if (supplyNoticeItemEntities.size() > 0) {
                        List<String> supplyNoticeIds = new ArrayList<>();
                        for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNoticeItemEntities) {
                            supplyNoticeIds.add(supplyNoticeItemEntity.getObjId());
                        }
                        supplyItemInfoList = supplyItemService.findAllByNoticeItemId(supplyNoticeIds);
                    }

                }
            }
            return supplyItemInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return supplyItemInfoList;
        }
    }

    /**
     * 通过单据编号查询活动通知单id
     *
     * @param activityNoticeDocNo
     * @return
     */
    @Override
    public List<String> findAllobjIdByDocNo(String activityNoticeDocNo) {
        List<String> objIds = new ArrayList<>();
        try {
            ActivityNoticeEntity activityNoticeEntity = activityNoticeRepository.findoneByDocNo("%" + activityNoticeDocNo + "%");
            objIds.add(activityNoticeEntity.getObjId());
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    /**
     * 根据活动通知ID，获取活动对应的车辆Items
     *
     * @param noticeId
     * @return
     */
    @Override
    public List<ActivityVehicleView> findActivityVehicleItemsByNoticeId(String noticeId) {
        return activityVehicleViewRepository.findActivityVehicleItemsByNoticeId(noticeId);
    }

    /**
     * 根据活动通知ID，获取活动对应的配件Items
     *
     * @param noticeId
     * @return
     */
    @Override
    public List<ActivityPartView> findActivityPartItemsByNoticeId(String noticeId) {
        return activityPartViewRepository.findActivityPartItemsByNoticeId(noticeId);
    }

    @Override
    public List<ActivityNoticeEntity> findAllByObjIds(Set<String> objIds) {
        return activityNoticeRepository.findAll(objIds);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean desertTask(String objId) {
        ActivityNoticeEntity entity = activityNoticeRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                activityNoticeRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityNoticeRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return activityNoticeRepository;
    }
}
