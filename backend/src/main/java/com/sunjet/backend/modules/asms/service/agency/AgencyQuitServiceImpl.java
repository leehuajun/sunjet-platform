package com.sunjet.backend.modules.asms.service.agency;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.agency.AgencyQuitRequestEntity;
import com.sunjet.backend.modules.asms.entity.agency.view.AgencyQuitRequestView;
import com.sunjet.backend.modules.asms.repository.agency.AgencyQuitRepostitory;
import com.sunjet.backend.modules.asms.repository.agency.AgencyQuitViewRepostitory;
import com.sunjet.backend.modules.asms.service.basic.AgencyService;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.agency.AgencyQuitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyQuitRequestItem;
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
 * 合作商退出申请单
 */
@Transactional
@Service("agencyQuitService")
public class AgencyQuitServiceImpl implements AgencyQuitService {
    @Autowired
    private AgencyQuitRepostitory agencyQuitRepostitory;
    @Autowired
    private AgencyQuitViewRepostitory agencyQuitViewRepostitory;

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
    public PageResult<AgencyQuitRequestView> getPageList(PageParam<AgencyQuitRequestItem> pageParam) {
        //1.查询条件
        AgencyQuitRequestItem agencyQuitRequestItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AgencyQuitRequestView> specification = null;
        if (agencyQuitRequestItem != null) {
            specification = Specifications.<AgencyQuitRequestView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(agencyQuitRequestItem.getSubmitter()), "submitter", agencyQuitRequestItem.getSubmitter())
                    .like(StringUtils.isNotBlank(agencyQuitRequestItem.getCode()), "code", "%" + agencyQuitRequestItem.getCode() + "%")
                    .eq(StringUtils.isNotBlank(agencyQuitRequestItem.getName()), "name", agencyQuitRequestItem.getName())
                    .eq(!agencyQuitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", agencyQuitRequestItem.getStatus())
                    .ge(agencyQuitRequestItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((agencyQuitRequestItem.getStartDate() != null && agencyQuitRequestItem.getEndDate() != null), "createdTime", new Range<Date>(agencyQuitRequestItem.getStartDate(), DateHelper.getEndDate(agencyQuitRequestItem.getEndDate())))
                    .build();
        }

        //3.执行查询
        Page<AgencyQuitRequestView> pages = agencyQuitViewRepostitory.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<AgencyQuitRequestInfo> rows = new ArrayList<>();
        //for (AgencyQuitRequestEntity entity : pages.getContent()) {
        //    AgencyQuitRequestInfo info = BeanUtils.copyPropertys(entity, new AgencyQuitRequestInfo());
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
            AgencyQuitRequestInfo agencyQuitRequestInfo = JsonHelper.map2Bean(variables.get("entity"), AgencyQuitRequestInfo.class);
            if (!agencyQuitRequestInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                AgencyQuitRequestEntity entity = BeanUtils.copyPropertys(agencyQuitRequestInfo, new AgencyQuitRequestEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, null, userInfo.getLogId());

                if (processInstanceInfo != null) {
                    //AgencyQuitRequestInfo agencyQuitRequest = findOne(agencyQuitRequestInfo.getObjId());
                    agencyQuitRequestInfo.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    agencyQuitRequestInfo.setStatus(DocStatus.AUDITING.getIndex());
                    save(agencyQuitRequestInfo);

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
        AgencyQuitRequestEntity agencyQuitRequestEntity = agencyQuitRepostitory.findOne(objId);
        try {
            if (processService.deleteProcessInstance(agencyQuitRequestEntity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(agencyQuitRequestEntity.getProcessInstanceId())) {
                agencyQuitRequestEntity.setStatus(DocStatus.OBSOLETE.getIndex());
                agencyQuitRequestEntity.setProcessInstanceId(null);
                agencyQuitRepostitory.save(agencyQuitRequestEntity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 保存一个实体
     *
     * @param agencyQuitRequestInfo
     * @return
     */
    @Override
    public AgencyQuitRequestInfo save(AgencyQuitRequestInfo agencyQuitRequestInfo) {
        try {
            if (agencyQuitRequestInfo != null && StringUtils.isBlank(agencyQuitRequestInfo.getDocNo())) {
                //获取单据编号
                String docNo = documentNoService.getDocumentNo(AgencyQuitRequestEntity.class.getSimpleName());
                agencyQuitRequestInfo.setDocNo(docNo);

            }
            AgencyInfo agencyInfo = agencyService.save(agencyQuitRequestInfo.getAgencyInfo());
            AgencyQuitRequestEntity entity = agencyQuitRepostitory.save(BeanUtils.copyPropertys(agencyQuitRequestInfo, new AgencyQuitRequestEntity()));


            agencyQuitRequestInfo = BeanUtils.copyPropertys(entity, agencyQuitRequestInfo);
            agencyQuitRequestInfo.setAgencyInfo(agencyInfo);
            return agencyQuitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 -->  通过对象
     *
     * @param agencyQuitRequestInfo
     * @return
     */
    @Override
    public boolean delete(AgencyQuitRequestInfo agencyQuitRequestInfo) {
        try {
            AgencyQuitRequestEntity entity = BeanUtils.copyPropertys(agencyQuitRequestInfo, new AgencyQuitRequestEntity());
            agencyQuitRepostitory.delete(entity);
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
            agencyQuitRepostitory.delete(objId);
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
    public AgencyQuitRequestInfo findOne(String objId) {
        try {
            AgencyQuitRequestEntity entity = agencyQuitRepostitory.findOne(objId);
            AgencyInfo agencyInfo = new AgencyInfo();
            if (StringUtils.isNotBlank(entity.getAgency())) {
                agencyInfo = agencyService.findOne(entity.getAgency());
            }
            AgencyQuitRequestInfo agencyQuitRequestInfo = BeanUtils.copyPropertys(entity, new AgencyQuitRequestInfo());
            agencyQuitRequestInfo.setAgencyInfo(agencyInfo);
            return agencyQuitRequestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyQuitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyQuitRepostitory;
    }

}
