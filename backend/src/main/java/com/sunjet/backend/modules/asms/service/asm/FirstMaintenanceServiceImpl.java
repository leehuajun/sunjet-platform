package com.sunjet.backend.modules.asms.service.asm;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.FirstMaintenanceEntity;
import com.sunjet.backend.modules.asms.entity.asm.GoOutEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.FirstMaintenanceView;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.repository.asm.FirstMaintenanceRepository;
import com.sunjet.backend.modules.asms.repository.asm.FirstMaintenanceViewRepository;
import com.sunjet.backend.modules.asms.repository.asm.GoOutRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.modules.asms.service.basic.VehicleService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.asm.FirstMaintenanceInfo;
import com.sunjet.dto.asms.asm.FirstMaintenanceItem;
import com.sunjet.dto.asms.asm.GoOutInfo;
import com.sunjet.dto.asms.basic.VehicleInfo;
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
import java.io.IOException;
import java.util.*;

/**
 * 首保
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("firstMaintenanceService")
public class FirstMaintenanceServiceImpl implements FirstMaintenanceService {

    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private FirstMaintenanceViewRepository firstMaintenanceViewRepository;

    @Autowired
    private GoOutRepository goOutRepository;//外出信息

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private DocumentNoService documentNoService;


    /**
     * 保存 实体
     *
     * @param firstMaintenanceInfo
     * @return
     */
    @Override
    public FirstMaintenanceInfo save(FirstMaintenanceInfo firstMaintenanceInfo) {
        if (firstMaintenanceInfo != null && StringUtils.isBlank(firstMaintenanceInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(FirstMaintenanceEntity.class.getSimpleName());
            firstMaintenanceInfo.setDocNo(docNo);

        }
        try {
            FirstMaintenanceEntity entity = BeanUtils.copyPropertys(firstMaintenanceInfo, new FirstMaintenanceEntity());
            //关联车辆信息
            VehicleInfo vehicleInfo = firstMaintenanceInfo.getVehicleInfo();
            if (vehicleInfo != null) {
                if (StringUtils.isNotBlank(vehicleInfo.getObjId())) {
                    //新增
                    if (StringUtils.isBlank(entity.getVehicleId())) {
                        entity.setVehicleId(vehicleInfo.getObjId());
                        vehicleInfo.setFmDate(new Date());
                        vehicleInfo = vehicleService.save(vehicleInfo);
                    } else {

                        if (!entity.getVehicleId().equals(vehicleInfo.getObjId())) {
                            VehicleInfo oldVehicle = vehicleService.findOne(entity.getVehicleId());
                            oldVehicle.setFmDate(null);
                            vehicleService.save(oldVehicle);
                            entity.setVehicleId(vehicleInfo.getObjId());
                            //设置新的首保车辆
                            entity.setVehicleId(vehicleInfo.getObjId());
                            vehicleInfo.setFmDate(new Date());
                            vehicleInfo = vehicleService.save(vehicleInfo);

                        }

                    }

                }

            }

            entity = firstMaintenanceRepository.save(entity);


            //关联外出信息
            List<GoOutInfo> goOutInfos = saveGoOutWithFirstMaintenance(entity.getObjId(), firstMaintenanceInfo.getGoOuts());
            FirstMaintenanceInfo newFirstMaintenance = BeanUtils.copyPropertys(entity, new FirstMaintenanceInfo());
            newFirstMaintenance.setGoOuts(goOutInfos);
            newFirstMaintenance.setVehicleInfo(vehicleInfo);
            return newFirstMaintenance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 外出信息
     *
     * @param firstMaintenanceId
     * @param goOuts
     */
    private List<GoOutInfo> saveGoOutWithFirstMaintenance(String firstMaintenanceId, List<GoOutInfo> goOuts) {
        List<GoOutInfo> entities = new ArrayList<>();
        if (goOuts != null && goOuts.size() > 0) {
            for (GoOutInfo info : goOuts) {
                info.setFirstMaintenanceId(firstMaintenanceId);//关联id
                GoOutEntity save = goOutRepository.save(BeanUtils.copyPropertys(info, new GoOutEntity()));
                GoOutInfo goOutInfo = BeanUtils.copyPropertys(save, new GoOutInfo());
                entities.add(goOutInfo);
            }
        }
        return entities;
    }

    /**
     * 删除 --> 通过 firstMaintenanceInfo 对象
     *
     * @param firstMaintenanceInfo
     * @return
     */
    @Override
    public boolean delete(FirstMaintenanceInfo firstMaintenanceInfo) {
        try {
            this.delete(firstMaintenanceInfo.getObjId());
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
            //删除关联数据
            goOutRepository.deleteByFirstMaintenance(objId);
            //恢复首保车辆时间
            FirstMaintenanceEntity firstMaintenanceEntity = firstMaintenanceRepository.findOne(objId);
            if (StringUtils.isNotBlank(firstMaintenanceEntity.getVehicleId())) {
                VehicleEntity vehicleEntity = vehicleRepository.findOne(firstMaintenanceEntity.getVehicleId());
                vehicleEntity.setFmDate(null);
                vehicleRepository.save(vehicleEntity);
            }

            //删除实体
            firstMaintenanceRepository.delete(objId);

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
    public FirstMaintenanceInfo findOne(String objId) {
        try {
            FirstMaintenanceEntity firstMaintenanceEntity = firstMaintenanceRepository.findOne(objId);
            return BeanUtils.copyPropertys(firstMaintenanceEntity, new FirstMaintenanceInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<FirstMaintenanceView> getPageList(PageParam<FirstMaintenanceItem> pageParam) {
        //1.查询条件
        FirstMaintenanceItem firstMaintenanceItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<FirstMaintenanceView> specification = null;
        if (firstMaintenanceItem != null) {
            specification = Specifications.<FirstMaintenanceView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(firstMaintenanceItem.getDocNo()), "docNo", firstMaintenanceItem.getDocNo())//单据编号
                    .eq(StringUtils.isNotBlank(firstMaintenanceItem.getDealerCode()), "dealerCode", firstMaintenanceItem.getDealerCode())// 服务站编号
                    .like(StringUtils.isNotBlank(firstMaintenanceItem.getDealerName()), "dealerName", firstMaintenanceItem.getDealerName())// 服务站名称
                    .eq(!firstMaintenanceItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", firstMaintenanceItem.getStatus())//表单状态
                    .ge(firstMaintenanceItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .like(StringUtils.isNotBlank(firstMaintenanceItem.getServiceManager()), "serviceManager", "%" + firstMaintenanceItem.getServiceManager() + "%")//服务经理
                    .like(StringUtils.isNotBlank(firstMaintenanceItem.getVin()), "vin", "%" + firstMaintenanceItem.getVin() + "%")
                    .like(StringUtils.isNotBlank(firstMaintenanceItem.getPlate()), "plate", "%" + firstMaintenanceItem.getPlate() + "%")
                    .like(StringUtils.isNotBlank(firstMaintenanceItem.getSender()), "sender", "%" + firstMaintenanceItem.getSender() + "%")
                    .between((firstMaintenanceItem.getStartDate() != null && firstMaintenanceItem.getEndDate() != null), "createdTime", new Range<Date>(firstMaintenanceItem.getStartDate(), DateHelper.getEndDate(firstMaintenanceItem.getEndDate())))
                    .build();
        }

        //3.执行查询
        Page<FirstMaintenanceView> pages = firstMaintenanceViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 获取首保服务单
     *
     * @param businessId 主键/业务id
     * @return
     */
    @Override
    public FirstMaintenanceInfo findOneWithGoOutsById(String businessId) {
        FirstMaintenanceEntity entity = firstMaintenanceRepository.findOne(businessId);
        return BeanUtils.copyPropertys(entity, new FirstMaintenanceInfo());
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
            FirstMaintenanceInfo firstMaintenanceInfo = JsonHelper.map2Bean(variables.get("entity"), FirstMaintenanceInfo.class);
            if (!firstMaintenanceInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                FirstMaintenanceEntity entity = BeanUtils.copyPropertys(firstMaintenanceInfo, new FirstMaintenanceEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                UserEntity serviceManager = userRepository.findOne(userInfo.getDealer().getServiceManagerId());

                variables.put("level", userInfo.getDealer().getLevel());
                variables.put("serviceManager", serviceManager.getLogId());
                if (StringUtils.isNotBlank(userInfo.getDealer().getParentId())) {
                    //一级服务站
                    DealerEntity parentDealer = dealerRepository.findOne(userInfo.getDealer().getParentId());

                    List<UserEntity> list = userRepository.findAllByDealerId(parentDealer.getObjId());
                    List<String> users = new ArrayList<>();
                    for (UserEntity userEntity : list) {
                        System.out.println(userEntity.getLogId());
                        users.add(userEntity.getLogId());
                    }
                    variables.put("firstLevelUsers", users);
                }
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程

                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    FirstMaintenanceInfo firstMaintenance = findOne(firstMaintenanceInfo.getObjId());
                    firstMaintenance.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    firstMaintenance.setStatus(DocStatus.AUDITING.getIndex());
                    save(firstMaintenance);

                    message.put("result", "提示");
                    message.put("message", "提交成功");
                    return message;
                } else {
                    message.put("result", "提示");
                    message.put("message", "提交失败");
                    return message;
                }
            } else {
                message.put("result", "提示");
                message.put("message", "提交失败");
            }

            return message;
        } catch (IOException e) {
            e.printStackTrace();
            message.put("result", "提示");
            message.put("message", "提交失败,请联系管理员");
            return message;
        }
    }

    /**
     * 通过车辆id获取首保id
     *
     * @param vehicleObjIds
     * @return
     */
    @Override
    public List<String> findAllIdByVehcicleIds(List<String> vehicleObjIds) {
        List<String> objIds = new ArrayList<>();
        try {
            List<FirstMaintenanceEntity> firstMaintenanceEntities = firstMaintenanceRepository.findAllByVehicleObjIds(vehicleObjIds);
            if (firstMaintenanceEntities != null && firstMaintenanceEntities.size() > 0) {
                for (FirstMaintenanceEntity firstMaintenanceEntity : firstMaintenanceEntities) {
                    objIds.add(firstMaintenanceEntity.getObjId());
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return firstMaintenanceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return firstMaintenanceRepository;
    }

    /**
     * 获取首保服务单
     *
     * @param vehicleId 主键/业务id
     * @return
     */
    @Override
    public FirstMaintenanceInfo findOneByVehicleId(String vehicleId) {
        FirstMaintenanceEntity entity = firstMaintenanceRepository.findOneByVehicleId(vehicleId);
        return BeanUtils.copyPropertys(entity, new FirstMaintenanceInfo());
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean desertTask(String objId) {
        FirstMaintenanceEntity entity = firstMaintenanceRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                VehicleEntity vehicleEntity = vehicleRepository.findOne(entity.getVehicleId());
                vehicleEntity.setFmDate(null);
                vehicleRepository.save(vehicleEntity);
                entity.setVehicleId(null);
                firstMaintenanceRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
