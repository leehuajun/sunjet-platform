package com.sunjet.backend.modules.asms.service.agency;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.agency.AgencyAdmitRequestEntity;
import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAdmitRequestView;
import com.sunjet.backend.modules.asms.entity.basic.AgencyEntity;
import com.sunjet.backend.modules.asms.repository.agency.AgencyAdmitRepostitory;
import com.sunjet.backend.modules.asms.repository.agency.AgencyAdmitViewRepostitory;
import com.sunjet.backend.modules.asms.repository.basic.AgencyRepository;
import com.sunjet.backend.modules.asms.service.basic.AgencyService;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestItem;
import com.sunjet.dto.asms.basic.AgencyInfo;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 * 合作商准入申请单
 */
@Transactional
@Service("agencyAdmitService")
public class AgencyAdmitServiceImpl implements AgencyAdmitService {
    @Autowired
    private AgencyAdmitRepostitory agencyAdmitRepostitory;
    @Autowired
    private AgencyAdmitViewRepostitory agencyAdmitViewRepostitory;

    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<AgencyAdmitRequestView> getPageList(PageParam<AgencyAdmitRequestItem> pageParam) {
        //1.查询条件
        AgencyAdmitRequestItem agencyAdmitRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencyAdmitRequestView> specification = null;
        if (agencyAdmitRequestItem != null) {
            specification = Specifications.<AgencyAdmitRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(agencyAdmitRequestItem.getSubmitter()), "submitter", agencyAdmitRequestItem.getSubmitter())
                    .like(StringUtils.isNotBlank(agencyAdmitRequestItem.getCode()), "code", "%" + agencyAdmitRequestItem.getCode() + "%")
                    .eq(StringUtils.isNotBlank(agencyAdmitRequestItem.getName()), "name", agencyAdmitRequestItem.getName())
                    .eq(!agencyAdmitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", agencyAdmitRequestItem.getStatus())
                    .ge(agencyAdmitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((agencyAdmitRequestItem.getStartDate() != null && agencyAdmitRequestItem.getEndDate() != null), "createdTime", new Range<Date>(agencyAdmitRequestItem.getStartDate(), DateHelper.getEndDate(agencyAdmitRequestItem.getEndDate())))
                    .build();
        }

        //3.执行查询
        Page<AgencyAdmitRequestView> pages = agencyAdmitViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

        ////4.数据转换
        //List<AgencyAdmitRequestInfo> rows = new ArrayList<>();
        //for (AgencyAdmitRequestEntity entity : pages.getContent()) {
        //    AgencyAdmitRequestInfo info = BeanUtils.copyPropertys(entity, new AgencyAdmitRequestInfo());
        //    rows.add(info);
        //}

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

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
            AgencyAdmitRequestInfo agencyAdmitRequestInfo = JsonHelper.map2Bean(variables.get("entity"), AgencyAdmitRequestInfo.class);
            if (!agencyAdmitRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                AgencyAdmitRequestEntity entity = BeanUtils.copyPropertys(agencyAdmitRequestInfo, new AgencyAdmitRequestEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, null, userInfo.getLogId());

                if (processInstanceInfo != null) {
                    //AgencyAdmitRequestInfo agencyAdmitRequest = findOne(agencyAdmitRequestInfo.getObjId());
                    //去掉没有用的流程变量
                    variables.remove("entity");
                    variables.remove("userInfo");
                    agencyAdmitRequestInfo.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    agencyAdmitRequestInfo.setStatus(DocStatus.AUDITING.getIndex());
                    save(agencyAdmitRequestInfo);

                    message.put("result", "提示");
                    message.put("message", "提交成功");
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
        AgencyAdmitRequestEntity agencyAdmitRequestEntity = agencyAdmitRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(agencyAdmitRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(agencyAdmitRequestEntity.getProcessInstanceId())) {
                agencyAdmitRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                agencyAdmitRequestEntity.setProcessInstanceId(null);
                agencyAdmitRepostitory.save(agencyAdmitRequestEntity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存
     *
     * @param agencyAdmitRequestInfo
     * @return
     */
    @Override
    public AgencyAdmitRequestInfo save(AgencyAdmitRequestInfo agencyAdmitRequestInfo) {
        try {
            if (agencyAdmitRequestInfo != null && StringUtils.isBlank(agencyAdmitRequestInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(AgencyAdmitRequestEntity.class.getSimpleName());
                agencyAdmitRequestInfo.setDocNo(docNo);

            }

            AgencyEntity agencyEntity = BeanUtils.copyPropertys(agencyAdmitRequestInfo.getAgencyInfo(), new AgencyEntity());

            agencyEntity = agencyRepository.save(agencyEntity);
            agencyAdmitRequestInfo.setAgency(agencyEntity.getObjId());
            AgencyAdmitRequestEntity entity = agencyAdmitRepostitory.save(BeanUtils.copyPropertys(agencyAdmitRequestInfo, new AgencyAdmitRequestEntity()));

            entity.setAgency(agencyEntity.getObjId());

            agencyAdmitRequestInfo = BeanUtils.copyPropertys(entity, agencyAdmitRequestInfo);
            agencyAdmitRequestInfo.getAgencyInfo().setObjId(agencyEntity.getObjId());
            agencyAdmitRequestInfo.setAgency(agencyEntity.getObjId());
            return agencyAdmitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 对象
     *
     * @param agencyAdmitRequestInfo
     * @return
     */
    @Override
    public boolean delete(AgencyAdmitRequestInfo agencyAdmitRequestInfo) {
        try {
            AgencyAdmitRequestEntity entity = BeanUtils.copyPropertys(agencyAdmitRequestInfo, new AgencyAdmitRequestEntity());
            agencyAdmitRepostitory.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 -->  通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            AgencyAdmitRequestEntity entity = agencyAdmitRepostitory.findOne(objId);
            agencyService.delete(entity.getAgency());
            agencyAdmitRepostitory.delete(objId);
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
    public AgencyAdmitRequestInfo findOne(String objId) {
        try {
            AgencyAdmitRequestEntity entity = agencyAdmitRepostitory.findOne(objId);
            AgencyInfo agencyInfo = new AgencyInfo();

            if (StringUtils.isNotBlank(entity.getAgency())) {
                AgencyEntity agencyEntity = agencyRepository.findOne(entity.getAgency());
                agencyInfo = BeanUtils.copyPropertys(agencyEntity, new AgencyInfo());
            }
            AgencyAdmitRequestInfo agencyAdmitRequestInfo = BeanUtils.copyPropertys(entity, new AgencyAdmitRequestInfo());
            agencyAdmitRequestInfo.setAgencyInfo(agencyInfo);
            return agencyAdmitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyAdmitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyAdmitRepostitory;
    }


}
