package com.sunjet.backend.modules.asms.service.asm;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.WarrantyMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceView;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.repository.asm.*;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyNoticeItemRepository;
import com.sunjet.backend.modules.asms.repository.supply.SupplyRepository;
import com.sunjet.backend.modules.asms.service.basic.DealerService;
import com.sunjet.backend.modules.asms.service.supply.SupplyNoticeService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.asm.*;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
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
import java.util.*;

/**
 * 三包服务单
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("warrantyMaintenanceService")
public class WarrantyMaintenanceServiceImpl implements WarrantyMaintenanceService {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;

    @Autowired
    private WarrantyMaintenanceViewRepository warrantyMaintenanceViewRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CommissionPartRepository commissionPartRepository;

    @Autowired
    private WarrantyMaintainRepository warrantyMaintainRepository;

    @Autowired
    private GoOutRepository goOutRepository;

    @Autowired
    private DealerService dealerService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplyNoticeService supplyNoticeService;

    @Autowired
    private SupplyItemRepository supplyItemRepository;

    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ProcessService processService;

    @Autowired
    private CommissionPartService commissionPartService;

    @Autowired
    private WarrantyMaintainService warrantyMaintainService;

    @Autowired
    private GoOutService goOutService;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return warrantyMaintenanceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return warrantyMaintenanceRepository;
    }


    /**
     * 保存三包实体
     *
     * @param warrantyMaintenanceInfo
     * @return
     */
    @Override
    public WarrantyMaintenanceInfo save(WarrantyMaintenanceInfo warrantyMaintenanceInfo) {
        try {

            WarrantyMaintenanceEntity entity = warrantyMaintenanceRepository.save(BeanUtils.copyPropertys(warrantyMaintenanceInfo, new WarrantyMaintenanceEntity()));


            // 保存三包车辆
            if (warrantyMaintenanceInfo.getVehicleInfo() != null && StringUtils.isNotBlank(warrantyMaintenanceInfo.getVehicleId())) {
                VehicleEntity vehicleEntity = BeanUtils.copyPropertys(warrantyMaintenanceInfo.getVehicleInfo(), new VehicleEntity());
                saveVehicle(vehicleEntity);
            }

            //保存三包配件需求列表
            //if (warrantyMaintenanceInfo.getCommissionParts() != null) {
            //    List<CommissionPartInfo> commissionPartInfos = saveCommissionParts(entity.getObjId(), warrantyMaintenanceInfo.getCommissionParts());
            //    warrantyMaintenanceInfo.setCommissionParts(commissionPartInfos);
            //}

            ////保存项目维修列表
            //if (warrantyMaintenanceInfo.getWarrantyMaintains() != null) {
            //    List<WarrantyMaintainInfo> warrantyMaintainInfos = saveWarrantyMaintains(entity.getObjId(), warrantyMaintenanceInfo.getWarrantyMaintains());
            //    warrantyMaintenanceInfo.setWarrantyMaintains(warrantyMaintainInfos);
            //}

            //保存外出信息
            //if (warrantyMaintenanceInfo.getGoOuts() != null) {
            //    List<GoOutInfo> goOutInfos = saveGoOuts(entity.getObjId(), warrantyMaintenanceInfo.getGoOuts());
            //    warrantyMaintenanceInfo.setGoOuts(goOutInfos);
            //}

            warrantyMaintenanceInfo = BeanUtils.copyPropertys(entity, warrantyMaintenanceInfo);


            return warrantyMaintenanceInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除 --> 通过 warrantyMaintenanceInfo 对象
     *
     * @param warrantyMaintenanceInfo
     * @return
     */
    @Override
    public boolean delete(WarrantyMaintenanceInfo warrantyMaintenanceInfo) {
        try {
            warrantyMaintenanceRepository.delete(warrantyMaintenanceInfo.getObjId());
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
            //删除关联的配件实体
            commissionPartRepository.deleteByWarrantyMaintenanceObjId(objId);
            //删除关联维修项目实体
            warrantyMaintainRepository.deleteByWarrantyMaintenanceObjId(objId);
            //删除关联外出信息实体
            goOutRepository.deleteByWarrantyMaintenanceId(objId);
            //删除实体
            warrantyMaintenanceRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<WarrantyMaintenanceView> getPageList(PageParam<WarrantyMaintenanceItem> pageParam) {
        //1.查询条件
        WarrantyMaintenanceItem warrantyMaintenanceItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<WarrantyMaintenanceView> specification = null;
        if (warrantyMaintenanceItem != null) {
            specification = Specifications.<WarrantyMaintenanceView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(warrantyMaintenanceItem.getDocNo()), "docNo", "%" + warrantyMaintenanceItem.getDocNo() + "%")
                    .eq(StringUtils.isNotBlank(warrantyMaintenanceItem.getDealerCode()), "dealerCode", warrantyMaintenanceItem.getDealerCode())// 服务站编号
                    .like(StringUtils.isNotBlank(warrantyMaintenanceItem.getDealerName()), "dealerName", "%" + warrantyMaintenanceItem.getDealerName() + "%")// 服务站名称
                    .eq(!warrantyMaintenanceItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", warrantyMaintenanceItem.getStatus())//表单状态
                    .ge(warrantyMaintenanceItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .like(StringUtils.isNotBlank(warrantyMaintenanceItem.getServiceManager()), "serviceManager", "%" + warrantyMaintenanceItem.getServiceManager() + "%")//服务经理
                    .eq(StringUtils.isNotBlank(warrantyMaintenanceItem.getVehicleId()), "vehicleId", warrantyMaintenanceItem.getVehicleId())//车辆id
                    .like(StringUtils.isNotBlank(warrantyMaintenanceItem.getPlate()), "plate", "%" + warrantyMaintenanceItem.getPlate() + "%")
                    .like(StringUtils.isNotBlank(warrantyMaintenanceItem.getSender()), "sender", "%" + warrantyMaintenanceItem.getSender() + "%")
                    .between("createdTime", new Range<Date>(warrantyMaintenanceItem.getStartDate(), DateHelper.getEndDate(warrantyMaintenanceItem.getEndDate())))
                    .build();
        }


        //3.执行查询
        Page<WarrantyMaintenanceView> pages = warrantyMaintenanceViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 通过单据类型查找一个三包单对象的信息
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public WarrantyMaintenanceInfo findOneWithOthersBySrcDocNo(String srcDocNo) {
        try {
            WarrantyMaintenanceInfo warrantyMaintenanceInfo = new WarrantyMaintenanceInfo();
            warrantyMaintenanceInfo = BeanUtils.copyPropertys(warrantyMaintenanceRepository.findOneWithOthersBySrcDocNo(srcDocNo), warrantyMaintenanceInfo);
            return warrantyMaintenanceInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public WarrantyMaintenanceInfo findOne(String objId) {
        try {
            WarrantyMaintenanceInfo warrantyMaintenanceInfo = new WarrantyMaintenanceInfo();
            warrantyMaintenanceInfo = BeanUtils.copyPropertys(warrantyMaintenanceRepository.findOne(objId), warrantyMaintenanceInfo);
            List<GoOutInfo> goOutInfoList = goOutService.findAllByWarrantyMaintenanceObjId(objId);
            if (goOutInfoList != null) {
                warrantyMaintenanceInfo.setGoOuts(goOutInfoList);
            }
            List<CommissionPartInfo> commissionPartInfoList = commissionPartService.findAllByWarrantyMaintenanceObjId(objId);
            if (commissionPartInfoList != null) {
                warrantyMaintenanceInfo.setCommissionParts(commissionPartInfoList);
            }
            List<WarrantyMaintainInfo> maintainInfoList = warrantyMaintainService.findAllByWarrantyMaintenanceObjId(objId);
            if (maintainInfoList != null) {
                warrantyMaintenanceInfo.setWarrantyMaintains(maintainInfoList);
            }

            return warrantyMaintenanceInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存三包车辆
     *
     * @param vehicleEntity
     */
    private void saveVehicle(VehicleEntity vehicleEntity) {
        try {
            vehicleRepository.save(vehicleEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///**
    // * 保存三包配件列表
    // *
    // * @param commissionParts
    // */
    //private List<CommissionPartInfo> saveCommissionParts(String warrantyMaintenanceObjId, List<CommissionPartInfo> commissionParts) {
    //    List<CommissionPartInfo> infos = new ArrayList<>();
    //    try {
    //        if (commissionParts != null) {
    //            for (CommissionPartInfo commissionPart : commissionParts) {
    //                if (StringUtils.isNotBlank(commissionPart.getPartCode())) {
    //                    commissionPart.setWarrantyMaintenance(warrantyMaintenanceObjId);
    //                    CommissionPartInfo commissionPartInfo = commissionPartService.save(commissionPart);
    //                    infos.add(commissionPartInfo);
    //                }
    //
    //            }
    //        }
    //        return infos;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return infos;
    //    }
    //
    //}


    ///**
    // * 保存维修项目列表
    // *
    // * @param warrantyMaintenanceObjId
    // * @param warrantyMaintains
    // */
    //private List<WarrantyMaintainInfo> saveWarrantyMaintains(String warrantyMaintenanceObjId, List<WarrantyMaintainInfo> warrantyMaintains) {
    //    List<WarrantyMaintainInfo> infos = new ArrayList<>();
    //    try {
    //        if (warrantyMaintains != null) {
    //            for (WarrantyMaintainInfo warrantyMaintain : warrantyMaintains) {
    //                warrantyMaintain.setWarrantyMaintenance(warrantyMaintenanceObjId);
    //                WarrantyMaintainInfo warrantyMaintainInfo = warrantyMaintainService.save(warrantyMaintain);
    //                infos.add(warrantyMaintainInfo);
    //            }
    //        }
    //        return infos;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return infos;
    //    }
    //
    //}

    /**
     * 保存三包外出项目
     *
     * @param warrantyMaintenanceObjId
     * @param goOuts
     */
    //private List<GoOutInfo> saveGoOuts(String warrantyMaintenanceObjId, List<GoOutInfo> goOuts) {
    //    List<GoOutInfo> infos = new ArrayList<>();
    //    try {
    //        if (StringUtils.isNotBlank(warrantyMaintenanceObjId)) {
    //            for (GoOutInfo goOut : goOuts) {
    //                goOut.setWarrantyMaintenance(warrantyMaintenanceObjId);
    //                GoOutInfo goOutInfo = goOutService.save(goOut);
    //                infos.add(goOutInfo);
    //            }
    //        }
    //        return infos;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return infos;
    //    }
    //}


    /**
     * 三包单提交流程
     *
     * @param variables
     * @return
     */
    @Override
    public Map<String, String> startProcess(Map<String, Object> variables) {
        //提交流程返回信息
        Map<String, String> message = new HashMap<>();
        try {
            WarrantyMaintenanceInfo warrantyMaintenanceRequest = JsonHelper.map2Bean(variables.get("entity"), WarrantyMaintenanceInfo.class);
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
            //校验单据状态
            if (warrantyMaintenanceRequest.getStatus() != DocStatus.CLOSED.getIndex()) {


                //检查是否禁用生成调拨单按钮
                if (warrantyMaintenanceRequest.getCommissionParts().size() > 0) {
                    for (CommissionPartInfo parts : warrantyMaintenanceRequest.getCommissionParts()) {
                        if (parts.getPartSupplyType().contains("调拨") && warrantyMaintenanceRequest.getSupplyNoticeId() != null) {
                            warrantyMaintenanceRequest.setCanEditSupply(false);
                        }
                    }
                } else {
                    warrantyMaintenanceRequest.setCanEditSupply(false);
                }
                if (userInfo.getDealer().getParentId() != null) {
                    List<UserEntity> list = userRepository.findAllByDealerId(userInfo.getDealer().getParentId());
                    if (list.size() == 0) {
                        message.put("result", "提示");
                        message.put("message", "提交失败");
                        return message;
                    }
                    List<String> users = new ArrayList<>();
                    for (UserEntity userEntity : list) {
                        users.add(userEntity.getLogId());
                    }
                    variables.put("firstLevelUsers", users);
                }
                variables.put("level", userInfo.getDealer().getLevel());
                String serviceManagerlogId = "";
                if (StringUtils.isNotBlank(userInfo.getDealer().getServiceManagerId())) {
                    UserEntity one = userRepository.findOne(userInfo.getDealer().getServiceManagerId());
                    serviceManagerlogId = one.getLogId();
                }
                //判断是否有服务经理
                if (StringUtils.isNotBlank(serviceManagerlogId)) {
                    variables.put("serviceManager", serviceManagerlogId);
                    //变更状态
                    WarrantyMaintenanceEntity warrantyMaintenanceEntity = warrantyMaintenanceRepository.findOne(warrantyMaintenanceRequest.getObjId());
                    //去掉没有用的流程变量
                    variables.remove("entity");
                    variables.remove("userInfo");
                    //WarrantyMaintenanceEntity warrantyMaintenanceEntity = BeanUtils.copyPropertys(warrantyMaintenanceRequest, new WarrantyMaintenanceEntity());
                    ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(warrantyMaintenanceEntity, variables, userInfo.getLogId());
                    if (processInstanceInfo != null) {

                        //WarrantyMaintenanceInfo warrantyMaintenance = findOne();
                        warrantyMaintenanceEntity.setStatus(DocStatus.AUDITING.getIndex());
                        warrantyMaintenanceEntity.setProcessInstanceId(processInstanceInfo.getId());
                        warrantyMaintenanceRepository.save(warrantyMaintenanceEntity);
                        //save(warrantyMaintenance);
                        message.put("result", "提示");
                        message.put("message", "提交成功");
                        return message;
                    }
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败请联系管理员");
                }
            } else {
                message.put("result", "提示");
                message.put("message", "提交失败请联系管理员");
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            message.put("result", "提示");
            message.put("message", "提交失败请联系管理员");
            return null;
        }


    }

    /**
     * 通过车辆id查询所有三包单id
     *
     * @param vehicleObjIds
     * @return
     */
    @Override
    public List<String> findAllObjIdsByVehicleId(List<String> vehicleObjIds) {
        List<String> objIds = new ArrayList<>();
        try {
            List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleObjIds(vehicleObjIds);
            if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                    objIds.add(warrantyMaintenanceEntity.getObjId());
                }
            }

            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    /**
     * 通过车辆ID查所有三包单
     *
     * @param vehicleId
     * @return
     */
    @Override
    public List<WarrantyMaintenanceInfo> findAllByVehicleId(String vehicleId) {
        List<WarrantyMaintenanceInfo> warrantyMaintenanceInfoList = new ArrayList<>();
        try {

            List<WarrantyMaintenanceEntity> warrantyMaintenanceEntities = warrantyMaintenanceRepository.findAllByVehicleId(vehicleId);
            if (warrantyMaintenanceEntities != null && warrantyMaintenanceEntities.size() > 0) {
                for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintenanceEntities) {
                    warrantyMaintenanceInfoList.add(BeanUtils.copyPropertys(warrantyMaintenanceEntity, new WarrantyMaintenanceInfo()));
                }
            }

            return warrantyMaintenanceInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return warrantyMaintenanceInfoList;
        }
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean desertTask(String objId) {
        WarrantyMaintenanceEntity entity = warrantyMaintenanceRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                warrantyMaintenanceRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}