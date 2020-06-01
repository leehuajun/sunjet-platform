package com.sunjet.backend.system.service;

import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.backend.modules.asms.entity.flow.CommentEntity;
import com.sunjet.dto.asms.flow.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by SUNJET_WS on 2017/9/5.
 */
public interface ProcessService {

    List<ProcessDefinitionInfo> findProcessDefinitionLastVersionList();


    List<DeploymentInfo> findDeploymentList();

    Boolean deleteProcessDefinitionByDeploymentId(String id);

    DeploymentInfo deploy(String flowName, InputStream InputStream);

    InputStream findImageInputStream(ProcessDefinitionInfo processDefinitionInfo);

    ProcessInstanceInfo startProcessInstance(FlowDocEntity entity, Map<String, Object> variables, String logId);

    InputStream findFlowBufferedImageByBusinessKey(String businessKey);

    List<CommentInfo> findCommentByBusinessKey(String businessKey);

    List<TaskInfo> findTaskByBusinessKey(String businessKey);

    Map<String, Object> findCoordingByTask(String taskId);

    List<CommentInfo> findCommentByTaskId(String taskId);

    List<TaskInfo> findAllTaskList(String logId);

    List<Task> findGroupTaskList(String userId);

    List<Task> findPersonalTaskList(String name);

    List<ProcessInstanceInfo> findProcessInstanceByIds(Set<String> processInstanceIds);

    ProcessInstance findProcessInstanceById(String processInstanceId);

    List<ProcessDefinitionInfo> findProcessDefinitionList();

    ProcessDefinitionInfo findProcessDefinitionById(String processDefinitionId);

    String findFormUrl(TaskInfo taskInfo);

    String findBusinessIdByTaskId(String taskId);

    String findBusinessKeyByTaskId(String taskId);

    List<String> findOutComeListByTaskId(String taskId);

    ProcessInstanceInfo completeTask(String taskId, String outcome, String comment, String userId, Map<String, Object> variables);

    ProcessInstanceInfo findProcessInstanceByBusinessKey(String businessKey);

    HistoricProcessInstanceInfo findHistoricProcessInstanceByBusinessKey(String businessKey);

    String findBusinessIdByProcessInstanceId(String processInstanceId);

    String findBusinessKeyByProcessInstanceId(String processInstanceId);

    List<TaskInfo> findWaitingTasks();

    CommentEntity saveComment(CommentEntity commentItem);

    List<HistoricProcessInstanceInfo> findHistoricTaskByLogId(String logId);

    List<HistoricTaskInstanceInfo> findHandleTaskByLogId(String logId);

    Boolean callBackTask(String historicTaskInstanceInfoId, String userId);

    Boolean deleteProcessInstance(String processInstanceId);

    Boolean deleteHistoricProcessInstance(String processInstanceId);

    List<CommentEntity> findCommentByProcessInstanceId(String processInstanceId);
}
