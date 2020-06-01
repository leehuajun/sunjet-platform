package com.sunjet.backend.modules.asms.service.agency;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.agency.AgencyAlterRequestEntity;
import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAlterRequestView;
import com.sunjet.backend.modules.asms.repository.agency.AgencyAlterRepostitory;
import com.sunjet.backend.modules.asms.repository.agency.AgencyAlterViewRepostitory;
import com.sunjet.backend.modules.asms.service.basic.AgencyService;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.agency.AgencyAlterRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAlterRequestItem;
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
 * Created by Administrator on 2016/9/5.
 * 合作商资质变更申请单
 */
@Transactional
@Service("agencyAlterService")
public class AgencyAlterServiceImpl implements AgencyAlterService {
    @Autowired
    private AgencyAlterRepostitory agencyAlterRepostitory;
    @Autowired
    private AgencyAlterViewRepostitory agencyAlterViewRepostitory;

    @Autowired
    private AgencyService agencyService;
    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<AgencyAlterRequestView> getPageList(PageParam<AgencyAlterRequestItem> pageParam) {
        //1.查询条件
        AgencyAlterRequestItem agencyAlterRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencyAlterRequestView> specification = null;
        if (agencyAlterRequestItem != null) {
            specification = Specifications.<AgencyAlterRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(agencyAlterRequestItem.getSubmitter()), "submitter", agencyAlterRequestItem.getSubmitter())
                    .like(StringUtils.isNotBlank(agencyAlterRequestItem.getCode()), "code", "%" + agencyAlterRequestItem.getCode() + "%")
                    .eq(StringUtils.isNotBlank(agencyAlterRequestItem.getName()), "name", agencyAlterRequestItem.getName())
                    .eq(!agencyAlterRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", agencyAlterRequestItem.getStatus())
                    .ge(agencyAlterRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((agencyAlterRequestItem.getStartDate() != null && agencyAlterRequestItem.getEndDate() != null), "createdTime", new Range<Date>(agencyAlterRequestItem.getStartDate(), DateHelper.getEndDate(agencyAlterRequestItem.getEndDate())))
                    .build();
        }

        //3.执行查询
        Page<AgencyAlterRequestView> pages = agencyAlterViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<AgencyAlterRequestInfo> rows = new ArrayList<>();
        //for (AgencyAlterRequestEntity entity : pages.getContent()) {
        //    AgencyAlterRequestInfo info = BeanUtils.copyPropertys(entity, new AgencyAlterRequestInfo());
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
            AgencyAlterRequestInfo agencyAlterRequestInfo = JsonHelper.map2Bean(variables.get("entity"), AgencyAlterRequestInfo.class);
            if (!agencyAlterRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                AgencyAlterRequestEntity entity = BeanUtils.copyPropertys(agencyAlterRequestInfo, new AgencyAlterRequestEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, null, userInfo.getLogId());

                if (processInstanceInfo != null) {
                    //AgencyAlterRequestInfo agencyAlterRequest = findOne(agencyAlterRequestInfo.getObjId());
                    agencyAlterRequestInfo.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    agencyAlterRequestInfo.setStatus(DocStatus.AUDITING.getIndex());
                    save(agencyAlterRequestInfo);

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
        AgencyAlterRequestEntity agencyAlterRequestEntity = agencyAlterRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(agencyAlterRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(agencyAlterRequestEntity.getProcessInstanceId())) {
                agencyAlterRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                agencyAlterRequestEntity.setProcessInstanceId(null);
                agencyAlterRepostitory.save(agencyAlterRequestEntity);
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
     * @param agencyAlterRequestInfo
     * @return
     */
    @Override
    public AgencyAlterRequestInfo save(AgencyAlterRequestInfo agencyAlterRequestInfo) {
        try {
            if (agencyAlterRequestInfo != null && StringUtils.isBlank(agencyAlterRequestInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(AgencyAlterRequestEntity.class.getSimpleName());
                agencyAlterRequestInfo.setDocNo(docNo);

            }
            //保存合作商
            AgencyInfo agencyInfo = agencyService.save(agencyAlterRequestInfo.getAgencyInfo());
            AgencyAlterRequestEntity entity = agencyAlterRepostitory.save(BeanUtils.copyPropertys(agencyAlterRequestInfo, new AgencyAlterRequestEntity()));
            agencyAlterRequestInfo = BeanUtils.copyPropertys(entity, agencyAlterRequestInfo);
            agencyAlterRequestInfo.setAgencyInfo(agencyInfo);

            return agencyAlterRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 对象
     *
     * @param agencyAlterRequestInfo
     * @return
     */
    @Override
    public boolean delete(AgencyAlterRequestInfo agencyAlterRequestInfo) {
        try {
            AgencyAlterRequestEntity entity = BeanUtils.copyPropertys(agencyAlterRequestInfo, new AgencyAlterRequestEntity());
            agencyAlterRepostitory.delete(entity);
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
            agencyAlterRepostitory.delete(objId);
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
    public AgencyAlterRequestInfo findOne(String objId) {
        try {
            AgencyAlterRequestEntity entity = agencyAlterRepostitory.findOne(objId);
            AgencyInfo agencyInfo = new AgencyInfo();
            if (StringUtils.isNotBlank(entity.getAgency())) {
                agencyInfo = agencyService.findOne(entity.getAgency());
            }

            AgencyAlterRequestInfo agencyAlterRequestInfo = BeanUtils.copyPropertys(entity, new AgencyAlterRequestInfo());
            agencyAlterRequestInfo.setAgencyInfo(agencyInfo);
            return agencyAlterRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyAlterRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyAlterRepostitory;
    }


}
