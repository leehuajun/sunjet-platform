package com.sunjet.backend.modules.asms.service.dealer;


import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAdmitRequestView;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAdmitRepostitory;
import com.sunjet.backend.modules.asms.repository.dealer.DealerAdmitViewRepostitory;
import com.sunjet.backend.system.Jpa.Specifications;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.system.service.UserService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestItem;
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
 * Created by Administrator on 2016/9/2.
 * 服务站准入申请
 */
@Transactional
@Service("dealerAdmitService")
public class DealerAdmitServiceImpl implements DealerAdmitService {
    @Autowired
    private DealerAdmitRepostitory dealerAdmitRepostitory;
    @Autowired
    private DealerAdmitViewRepostitory dealerAdmitViewRepostitory;
    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProcessService processService;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<DealerAdmitRequestInfo> findAll() {
        try {
            List<DealerAdmitRequestEntity> list = this.dealerAdmitRepostitory.findAll();
            List<DealerAdmitRequestInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (DealerAdmitRequestEntity dealerAdmitRequestEntity : list) {
                    DealerAdmitRequestInfo info = new DealerAdmitRequestInfo();
                    infos.add(BeanUtils.copyPropertys(dealerAdmitRequestEntity, info));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<DealerAdmitRequestView> getPageList(PageParam<DealerAdmitRequestItem> pageParam) {

        //1.查询条件
        DealerAdmitRequestItem dealerAdmitRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DealerAdmitRequestView> specification = null;
        Specification<DealerAdmitRequestView> specification1 = null;
        Specification<DealerAdmitRequestView> specification2 = null;
        if (dealerAdmitRequestItem != null) {
            specification1 = Specifications.<DealerAdmitRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .predicate(StringUtils.isNotBlank(dealerAdmitRequestItem.getSubmitter()),
                            Specifications.or()
                                    .eq(StringUtils.isNotBlank(dealerAdmitRequestItem.getSubmitter()), "submitter", dealerAdmitRequestItem.getSubmitter())
                                    .eq(StringUtils.isNotBlank(dealerAdmitRequestItem.getServiceManagerId()), "serviceManagerId", dealerAdmitRequestItem.getServiceManagerId())
                                    .build())
                    .like(StringUtils.isNotBlank(dealerAdmitRequestItem.getCode()), "code", dealerAdmitRequestItem.getCode())
                    .like(StringUtils.isNotBlank(dealerAdmitRequestItem.getName()), "name", dealerAdmitRequestItem.getName())
                    .eq(StringUtils.isNotBlank(dealerAdmitRequestItem.getProvinceId()), "provinceId", dealerAdmitRequestItem.getProvinceId())
                    .eq(!dealerAdmitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", dealerAdmitRequestItem.getStatus())
                    .ge(dealerAdmitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .eq(StringUtils.isNotBlank(dealerAdmitRequestItem.getStar()), "star", dealerAdmitRequestItem.getStar())
                    .eq(StringUtils.isNotBlank(dealerAdmitRequestItem.getQualification()), "qualification", dealerAdmitRequestItem.getQualification())
                    //.eq(StringUtils.isNotBlank(dealerAdmitRequestItem.getServiceManagerId()), "serviceManagerId", dealerAdmitRequestItem.getServiceManagerId())
                    .between((dealerAdmitRequestItem.getStartDate() != null && dealerAdmitRequestItem.getEndDate() != null), "createdTime", new Range<Date>(dealerAdmitRequestItem.getStartDate(), dealerAdmitRequestItem.getEndDate()))
                    .build();
            specification2 = Specifications.<DealerAdmitRequestView>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .build();
            specification = Specifications.<DealerAdmitRequestView>or()
                    .predicate(specification1)
                    .predicate(specification2)
                    .build();
        }


        //3.执行查询
        Page<DealerAdmitRequestView> pages = dealerAdmitViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
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
            DealerAdmitRequestInfo dealerAdmitRequestInfo = JsonHelper.map2Bean(variables.get("entity"), DealerAdmitRequestInfo.class);
            DealerAdmitRequestEntity entity = BeanUtils.copyPropertys(dealerAdmitRequestInfo, new DealerAdmitRequestEntity());
            UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
            ProcessInstanceInfo processInstanceInfo = null;
            //去掉没有用的流程变量
            variables.remove("entity");
            variables.remove("userInfo");

            //校验状态
            if (!dealerAdmitRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                if (userInfo.getDealer() != null) {
                    if (StringUtils.isBlank(userInfo.getDealer().getServiceManagerName())) {
                        message.put("result", "提示");
                        message.put("message", "当前服务站没有服务经理");
                        return message;
                    }
                    variables.put("submitter", "dealer");
                    variables.put("serviceManager", userService.findOne(userInfo.getDealer().getServiceManagerId()).getLogId());
                    //获取流程实例
                    processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                } else {
                    variables.put("submitter", "none");
                    // 获取流程实例
                    processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                }

                if (processInstanceInfo != null) {
                    DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitRepostitory.findOne(dealerAdmitRequestInfo.getObjId());
                    dealerAdmitRequest.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态
                    dealerAdmitRequest.setStatus(DocStatus.AUDITING.getIndex());
                    dealerAdmitRepostitory.save(dealerAdmitRequest);
                    message.put("result", "提示");
                    message.put("message", "提交成功");
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
            message.put("message", "提交失败");
            return message;
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
        DealerAdmitRequestEntity dealerAdmitRequestEntity = dealerAdmitRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(dealerAdmitRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(dealerAdmitRequestEntity.getProcessInstanceId())) {
                dealerAdmitRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                dealerAdmitRequestEntity.setProcessInstanceId(null);
                //dealerRepository.delete(dealerAdmitRequestEntity.getDealer());
                //dealerAdmitRequestEntity.setDealer(null);
                dealerAdmitRepostitory.save(dealerAdmitRequestEntity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public DealerAdmitRequestEntity findOneById(String objId) {
        try {
            DealerAdmitRequestEntity entity = dealerAdmitRepostitory.findOne(objId);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param dealerAdmitRequestInfo
     * @return
     */
    @Override
    public DealerAdmitRequestInfo save(DealerAdmitRequestInfo dealerAdmitRequestInfo) {
        try {
            DealerEntity dealerEntity = BeanUtils.copyPropertys(dealerAdmitRequestInfo.getDealerInfo(), new DealerEntity());
            dealerEntity = dealerRepository.save(dealerEntity);
            dealerAdmitRequestInfo.setDealer(dealerEntity.getObjId());
            DealerAdmitRequestEntity entity = dealerAdmitRepostitory.save(BeanUtils.copyPropertys(dealerAdmitRequestInfo, new DealerAdmitRequestEntity()));
            entity.setDealer(dealerEntity.getObjId());
            dealerAdmitRequestInfo = BeanUtils.copyPropertys(entity, dealerAdmitRequestInfo);
            dealerAdmitRequestInfo.getDealerInfo().setObjId(dealerEntity.getObjId());
            dealerAdmitRequestInfo.setDealer(dealerEntity.getObjId());
            return dealerAdmitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 dealerAdmitRequestInfo 对象
     *
     * @param dealerAdmitRequestInfo
     * @return
     */
    @Override
    public boolean delete(DealerAdmitRequestInfo dealerAdmitRequestInfo) {
        try {
            DealerAdmitRequestEntity entity = new DealerAdmitRequestEntity();
            entity = BeanUtils.copyPropertys(dealerAdmitRequestInfo, entity);
            dealerAdmitRepostitory.delete(entity);
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
    public boolean deleteByObjId(String objId) {
        try {
            dealerAdmitRepostitory.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerAdmitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerAdmitRepostitory;
    }
}
