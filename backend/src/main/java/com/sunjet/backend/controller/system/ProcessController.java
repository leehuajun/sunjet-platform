package com.sunjet.backend.controller.system;

import com.sunjet.backend.modules.asms.entity.flow.CommentEntity;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.dto.asms.flow.*;
import com.sunjet.utils.common.IOUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by SUNJET_WS on 2017/9/5.
 * 流程引擎
 */
@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    ProcessService processService;


    /**
     * 获取最新的流程定义版本
     *
     * @return
     */
    @PostMapping("findProcessDefinitionLastVersionList")
    public List<ProcessDefinitionInfo> findProcessDefinitionLastVersionList() {
        return processService.findProcessDefinitionLastVersionList();
    }

    /**
     * 获取部署对象
     *
     * @return
     */
    @PostMapping("findDeploymentList")
    public List<DeploymentInfo> findDeploymentList() {
        return processService.findDeploymentList();
    }

    /**
     * 删除部署对象
     *
     * @param id
     * @return
     */
    @DeleteMapping("deleteProcessDefinitionByDeploymentId")
    public Boolean deleteProcessDefinitionByDeploymentId(@RequestBody String id) {
        return processService.deleteProcessDefinitionByDeploymentId(id);
    }

    /**
     * 部署流程
     *
     * @param fileName
     * @return
     */
    @PostMapping("deploy")
    public DeploymentInfo deploy(String fileName, MultipartFile file) {
        try {
            return processService.deploy(fileName, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查看流程图
     *
     * @param processDefinitionInfo
     * @return
     */
    @PostMapping("findImageInputStream")
    public byte[] findImageInputStream(@RequestBody ProcessDefinitionInfo processDefinitionInfo) {

        InputStream inputStream = null;
        inputStream = processService.findImageInputStream(processDefinitionInfo);
        try {
            byte[] bytes = IOUtils.input2byte(inputStream);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过businessKey查找流程图
     *
     * @param businessKey
     * @return
     */
    @ApiOperation(value = "通过buinesskey查找流程图")
    @PostMapping("findFlowBufferedImageByBusinessKey")
    public byte[] findFlowBufferedImageByBusinessKey(@RequestBody String businessKey) {
        try {
            InputStream inputStream = processService.findFlowBufferedImageByBusinessKey(businessKey);
            if (inputStream != null) {
                byte[] bytes = IOUtils.input2byte(inputStream);
                return bytes;
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过businessKey查找审批记录
     *
     * @param businessKey
     * @return
     */
    @ApiOperation(value = "通过businessKey查找审批记录")
    @PostMapping("findCommentByBusinessKey")
    public List<CommentInfo> findCommentByBusinessKey(@RequestBody String businessKey) {
        return processService.findCommentByBusinessKey(businessKey);
    }

    /**
     * 通过businessKey查找流程任务
     *
     * @param businessKey
     * @return
     */
    @ApiOperation(value = "通过businessKey查找流程任务")
    @PostMapping("findTaskByBusinessKey")
    public List<TaskInfo> findTaskByBusinessKey(@RequestBody String businessKey) {
        return processService.findTaskByBusinessKey(businessKey);
    }

    /**
     * 通过taskId 获取任务坐标
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "通过taskId 获取任务坐标")
    @PostMapping("findCoordingByTask")
    public Map<String, Object> findCoordingByTask(@RequestBody String taskId) {
        return processService.findCoordingByTask(taskId);
    }

    /**
     * 通过taskId获取审批记录列表
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "通过taskId获取审批记录列表")
    @PostMapping("findCommentByTaskId")
    public List<CommentInfo> findCommentByTaskId(@RequestBody String taskId) {
        return processService.findCommentByTaskId(taskId);
    }


    /**
     * 通过logId 查询个人任务列表
     *
     * @param logId
     * @return
     */
    @ApiOperation(value = "通过logId 查询个人任务列表")
    @PostMapping("findAllTaskList")
    public List<TaskInfo> findAllTaskList(@RequestBody String logId) {
        return processService.findAllTaskList(logId);
    }

    /**
     * 通过流程实例id列表查询
     *
     * @param processInstanceIds
     * @return
     */
    @ApiOperation(value = "通过流程实例id列表查询")
    @PostMapping("findProcessInstanceByIds")
    public List<ProcessInstanceInfo> findProcessInstanceByIds(@RequestBody Set<String> processInstanceIds) {
        return processService.findProcessInstanceByIds(processInstanceIds);
    }


    /**
     * 查询流程定义的信息，对应表（act_re_procdef）
     *
     * @return 所有流程定义集合，包括老版本的流程定义
     */
    @ApiOperation(value = "获取所有流程定义集合，包括老版本的流程定义")
    @PostMapping("findProcessDefinitionList")
    public List<ProcessDefinitionInfo> findProcessDefinitionList() {
        return processService.findProcessDefinitionList();
    }

    /**
     * 通过流程定义Id查询流程定义
     *
     * @param ProcessDefinitionId
     * @return
     */
    @ApiOperation(value = "通过流程定义Id查询流程定义")
    @PostMapping("findProcessDefinitionById")
    public ProcessDefinitionInfo findProcessDefinitionById(@RequestBody String ProcessDefinitionId) {
        return processService.findProcessDefinitionById(ProcessDefinitionId);
    }

    /**
     * 根据任务对象，获取流程实例的FormUrl
     *
     * @return
     */
    @ApiOperation(value = "根据任务对象，获取流程实例的FormUrl")
    @PostMapping("findFormUrl")
    public String findFormUrl(@RequestBody TaskInfo taskInfo) {
        return processService.findFormUrl(taskInfo);
    }


    /**
     * 使用任务ID，查找业务ID
     *
     * @param taskId
     * @return 业务ID
     */
    @ApiOperation(value = "使用任务ID，查找业务ID")
    @PostMapping("findBusinessIdByTaskId")
    public String findBusinessIdByTaskId(@RequestBody String taskId) {
        return processService.findBusinessIdByTaskId(taskId);
    }


    /**
     * 已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中")
    @PostMapping("findOutComeListByTaskId")
    public List<String> findOutComeListByTaskId(@RequestBody String taskId) {
        return processService.findOutComeListByTaskId(taskId);
    }

    /**
     * 完成审批任务
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "完成审批任务")
    @PostMapping("completeTask")
    public ProcessInstanceInfo completeTask(@RequestBody Map<String, Object> map) {
        return processService.completeTask((String) map.get("taskId"), (String) map.get("outcome"), (String) map.get("comment"), (String) map.get("userId"), (Map<String, Object>) map.get("variables"));
    }

    /**
     * 根据BusinessKey查找ProcessInstance
     *
     * @param businessKey
     * @return
     */
    @ApiOperation(value = "根据BusinessKey查找ProcessInstance")
    @PostMapping("findProcessInstanceByBusinessKey")
    public ProcessInstanceInfo findProcessInstanceByBusinessKey(@RequestBody String businessKey) {
        return processService.findProcessInstanceByBusinessKey(businessKey);
    }


    /**
     * 根据BusinessKey查找HistoricProcessInstance
     *
     * @param businessKey
     * @return
     */
    @ApiOperation(value = "根据BusinessKey查找HistoricProcessInstance")
    @PostMapping("findHistoricProcessInstanceByBusinessKey")
    public HistoricProcessInstanceInfo findHistoricProcessInstanceByBusinessKey(@RequestBody String businessKey) {
        return processService.findHistoricProcessInstanceByBusinessKey(businessKey);
    }

    @ApiOperation(value = "获取等待执行的任务")
    @PostMapping("findWaitingTasks")
    public List<TaskInfo> findWaitingTasks() {
        return processService.findWaitingTasks();
    }


    /**
     * 保存审批记录
     *
     * @param commentEntity
     * @return
     */
    @ApiOperation(value = "保存审批日记")
    @PostMapping("saveComment")
    public CommentEntity saveComment(@RequestBody CommentEntity commentEntity) {
        return processService.saveComment(commentEntity);
    }

    /**
     * 通过logId 获取历史完成任务
     *
     * @return
     */
    @ApiOperation(value = "通过logId 获取历史完成任务")
    @PostMapping("findHistoricTaskByLogId")
    public List<HistoricProcessInstanceInfo> findHistoricTaskByLogId(@RequestBody String LogId) {
        return processService.findHistoricTaskByLogId(LogId);
    }


    /**
     * 通过logId 获取已处理任务
     *
     * @return
     */
    @ApiOperation(value = "通过logId 获取已处理任务")
    @PostMapping("findHandleTaskByLogId")
    public List<HistoricTaskInstanceInfo> findHandleTaskByLogId(@RequestBody String LogId) {
        return processService.findHandleTaskByLogId(LogId);
    }


    /**
     * 撤回任务
     *
     * @return
     */
    @ApiOperation(value = "撤回任务")
    @PostMapping("callBackTask")
    public Boolean callBackTask(@RequestBody Map<String, Object> map) {
        return processService.callBackTask(map.get("historicTaskInstanceInfoId").toString(), map.get("userId").toString());
    }

    /**
     * 通过ProcessInstanceId查找审批日记
     *
     * @return
     */
    @ApiOperation(value = "通过ProcessInstanceId查找审批日记")
    @PostMapping("/findCommentByProcessInstanceId")
    public List<CommentEntity> findCommentByProcessInstanceId(@RequestBody String ProcessInstanceId) {
        return processService.findCommentByProcessInstanceId(ProcessInstanceId);
    }


}
