package com.sunjet.backend.quartz;

import com.sunjet.backend.system.entity.UserEntity;
import com.sunjet.backend.system.repository.MailRepository;
import com.sunjet.backend.system.repository.UserRepository;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.dto.asms.flow.ProcessDefinitionInfo;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.transaction.Transactional;
import java.util.*;

/**
 * 定时任务邮件提醒
 */
@DisallowConcurrentExecution
@Slf4j
public class TaskMailJob implements Job {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ProcessService processService;
    @Autowired
    private MailRepository mailRepository;

    @Value("${spring.mail.username}")
    private String username;

    @Transactional(value = Transactional.TxType.NEVER)
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //流程定义map
        Map<String, ProcessDefinitionInfo> mapProcessDefinition = new HashMap<>();

        List<ProcessDefinitionInfo> list = processService.findProcessDefinitionList();
        for (ProcessDefinitionInfo pd : list) {
            mapProcessDefinition.put(pd.getId(), pd);
        }


        //查询所有用户
        List<UserEntity> userEntityList = userRepository.findAllByEnabledAndEmailIsNotNull();
        for (UserEntity userEntity : userEntityList) {
            List<TaskInfo> tasks = processService.findAllTaskList(userEntity.getLogId());
            //如果有任务和已经填写了e-mail地址才发邮件
            if (tasks != null && tasks.size() > 0 && userEntity.getEmail() != null) {
                //流程实例map
                Map<String, ProcessInstanceInfo> processInstanceInfoHashMap = new HashMap<>();
                for (TaskInfo task : tasks) {
                    Set<String> processInstanceIds = new HashSet<>();
                    processInstanceIds.add(task.getProcessInstanceId());
                    if (processInstanceIds.size() > 0) {
                        //流程实例集合
                        List<ProcessInstanceInfo> processInstances = processService.findProcessInstanceByIds(processInstanceIds);
                        for (ProcessInstanceInfo processInstance : processInstances) {
                            processInstanceInfoHashMap.put(processInstance.getId(), processInstance);
                        }
                    }
                }

                for (TaskInfo task : tasks) {
                    if (task.getCreateTime().getTime() > userEntity.getEmailSendDate().getTime()) {
                        String docNo = "";
                        String docType = "";
                        try {
                            SimpleMailMessage message = new SimpleMailMessage();
                            message.setFrom(username);
                            message.setTo(userEntity.getEmail());
                            //单据编号
                            docNo = processInstanceInfoHashMap.get(task.getProcessInstanceId()).getBusinessKey().split("\\.")[2];
                            message.setSubject("DMS售后服务系统   待处理单据:" + docNo);
                            //单据类型
                            docType = mapProcessDefinition.get(task.getProcessDefinitionId()).getName();
                            message.setText("待办任务提醒：DMS售后服务系统——" + docType + docNo + "需要您办理，请查阅");
                            javaMailSender.send(message);
                        } catch (MailException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //更新发送时间
                userEntity.setEmailSendDate(new Date());
                userRepository.save(userEntity);

            }

        }
    }
}
