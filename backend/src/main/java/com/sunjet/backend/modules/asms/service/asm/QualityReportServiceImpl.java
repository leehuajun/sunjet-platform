package com.sunjet.backend.modules.asms.service.asm;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.asm.QualityReportEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.QualityReportView;
import com.sunjet.backend.modules.asms.entity.basic.DealerEntity;
import com.sunjet.backend.modules.asms.repository.asm.QualityReportRepository;
import com.sunjet.backend.modules.asms.repository.asm.QualityReportViewRepository;
import com.sunjet.backend.modules.asms.repository.basic.DealerRepository;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.asm.QualityReportInfo;
import com.sunjet.dto.asms.asm.QualityReprotItem;
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
 * 售后质量速报
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("qualityReportService")
public class QualityReportServiceImpl implements QualityReportService {
    @Autowired
    private QualityReportRepository qualityReportRepository;
    @Autowired
    private QualityReportViewRepository qualityReportViewRepository;
    @Autowired
    private ReportVehicleService reportVehicleService;
    @Autowired
    private ReportPartService reportPartService;
    @Autowired
    private DocumentNoService documentNoService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public QualityReportEntity save(QualityReportEntity qualityReportEntity) {
        if (qualityReportEntity != null && StringUtils.isBlank(qualityReportEntity.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(QualityReportEntity.class.getSimpleName());
            qualityReportEntity.setDocNo(docNo);

        }
        return qualityReportRepository.save(qualityReportEntity);
    }

    /**
     * 删除 --> 通过 qualityReportInfo 对象
     *
     * @param qualityReportInfo
     * @return
     */
    @Override
    public boolean delete(QualityReportInfo qualityReportInfo) {
        try {
            qualityReportRepository.delete(qualityReportInfo.getObjId());
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
            qualityReportRepository.delete(objId);
            reportVehicleService.deleteByQualityReportObjId(objId);
            reportPartService.deleteByQualityReportObjId(objId);
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
    public QualityReportInfo findOne(String objId) {
        try {
            QualityReportEntity qualityReportEntity = qualityReportRepository.findOne(objId);
            return BeanUtils.copyPropertys(qualityReportEntity, new QualityReportInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<QualityReprotItem> getPageList(PageParam<QualityReprotItem> pageParam) {
        //1.查询条件
        QualityReprotItem qualityReprotItem = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<QualityReportView> specification = null;
        if (qualityReprotItem != null) {
            specification = Specifications.<QualityReportView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(qualityReprotItem.getDocNo()), "docNo", "%" + qualityReprotItem.getDocNo() + "%")//单据编号
                    .like(StringUtils.isNotEmpty(qualityReprotItem.getVehicleType()), "vehicleType", "%" + qualityReprotItem.getVehicleType() + "%")//车辆类型
                    .eq(StringUtils.isNotEmpty(qualityReprotItem.getDealerCode()), "dealerCode", qualityReprotItem.getDealerCode())
                    .eq(StringUtils.isNotEmpty(qualityReprotItem.getDealerName()), "dealerName", qualityReprotItem.getDealerName())
                    .eq(StringUtils.isNotEmpty(qualityReprotItem.getServiceManager()), "serviceManager", qualityReprotItem.getServiceManager())
                    .eq(!qualityReprotItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", qualityReprotItem.getStatus())//表单状态
                    .ge(qualityReprotItem.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .between((qualityReprotItem.getStartDate() != null && qualityReprotItem.getEndDate() != null), "createdTime", new Range<Date>(qualityReprotItem.getStartDate(), DateHelper.getEndDate(qualityReprotItem.getEndDate())))
                    .like((StringUtils.isNotBlank(qualityReprotItem.getVin())), "vin", "%" + qualityReprotItem.getVin() + "%")
                    .build();
        }

        //3.执行查询
        Page<QualityReportView> pages = qualityReportViewRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        //List<QualityReportInfo> rows = new ArrayList<>();
        //for (QualityReportEntity entity : pages.getContent()) {
        //    QualityReportInfo info = entityToInfo(entity);
        //    rows.add(info);
        //}

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 关键字搜索质量速报
     *
     * @param keyword
     * @param dealerCode
     * @return
     */
    @Override
    public List<QualityReportInfo> findAllByKeywordAndDealerCode(String keyword, String dealerCode) {
        List<QualityReportEntity> qualityReportEntityList = qualityReportViewRepository.findAllByKeywordAndDealerCode("%" + keyword + "%", dealerCode);
        List<QualityReportInfo> qualityReportInfoList = new ArrayList<>();
        for (QualityReportEntity qualityReportEntity : qualityReportEntityList) {
            qualityReportInfoList.add(BeanUtils.copyPropertys(qualityReportEntity, new QualityReportInfo()));
        }

        return qualityReportInfoList;
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
            QualityReportInfo qualityReportInfo = JsonHelper.map2Bean(variables.get("entity"), QualityReportInfo.class);
            if (!qualityReportInfo.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                QualityReportEntity entity = BeanUtils.copyPropertys(qualityReportInfo, new QualityReportEntity());
                UserInfo userInfo = JsonHelper.map2Bean(variables.get("userInfo"), UserInfo.class);


                DealerEntity dealerEntity = dealerRepository.findOneByCode(qualityReportInfo.getDealerCode());
                UserEntity userEntity = userRepository.findOne(dealerEntity.getServiceManagerId());
                if (userEntity == null) {
                    message.put("result", "提示");
                    message.put("message", "提交成功");
                    return message;
                }
                variables.put("serviceManager", userEntity.getLogId());
                //去掉没有用的流程变量
                variables.remove("entity");
                variables.remove("userInfo");
                //启动流程
                ProcessInstanceInfo processInstanceInfo = processService.startProcessInstance(entity, variables, userInfo.getLogId());
                if (processInstanceInfo != null) {
                    entity = qualityReportRepository.findOne(qualityReportInfo.getObjId());
                    entity.setProcessInstanceId(processInstanceInfo.getId());
                    //变更状态

                    entity.setStatus(DocStatus.AUDITING.getIndex());
                    save(entity);

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
        QualityReportEntity entity = qualityReportRepository.findOne(objId);
        try {
            if (processService.deleteProcessInstance(entity.getProcessInstanceId()) || processService.deleteHistoricProcessInstance(entity.getProcessInstanceId())) {
                entity.setStatus(DocStatus.OBSOLETE.getIndex());
                entity.setProcessInstanceId(null);
                qualityReportRepository.save(entity);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return qualityReportRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return qualityReportRepository;
    }
}
