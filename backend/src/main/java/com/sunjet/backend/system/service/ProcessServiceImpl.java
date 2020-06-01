package com.sunjet.backend.system.service;

import com.sunjet.backend.base.FlowDocEntity;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.flow.CommentEntity;
import com.sunjet.backend.system.repository.CommentRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.flow.*;
import lombok.Getter;
import lombok.Setter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * Created by SUNJET_WS on 2017/9/5.
 * 流程引擎
 */
@Transactional
@Service("processService")
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ApplicationContext applicationContext;
    @Getter
    @Setter
    private BaseService baseService;


    /**
     * 查询流程定义最后版本的信息，对应表（act_re_procdef）
     *
     * @return 最新版版本的流程定义集合
     */
    @Override
    public List<ProcessDefinitionInfo> findProcessDefinitionLastVersionList() {
        List<ProcessDefinitionInfo> processDefinitionInfoList = new ArrayList<>();
        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                    .latestVersion()
                    .list();
            for (ProcessDefinition processDefinition : list) {
                processDefinitionInfoList.add(processDefinitionToInfo(processDefinition));
            }
            return processDefinitionInfoList;

        } catch (Exception e) {
            e.printStackTrace();
            return processDefinitionInfoList;
        }
    }


    /**
     * 获取部署对象
     *
     * @return
     */
    @Override
    public List<DeploymentInfo> findDeploymentList() {
        List<DeploymentInfo> deploymentInfoList = new ArrayList<>();
        try {
            List<Deployment> deploymentList = repositoryService.createDeploymentQuery()//创建部署对象查询
                    .orderByDeploymenTime().desc()//
                    .list();

            for (Deployment deployment : deploymentList) {
                DeploymentInfo deploymentInfo = new DeploymentInfo();
                deploymentInfo.setId(deployment.getId());
                deploymentInfo.setCategory(deployment.getCategory());
                deploymentInfo.setDeploymentTime(deployment.getDeploymentTime());
                deploymentInfo.setName(deployment.getName());
                deploymentInfo.setTenantId(deployment.getTenantId());
                deploymentInfoList.add(deploymentInfo);

            }

            return deploymentInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return deploymentInfoList;
        }
    }

    /**
     * 删除部署对象
     *
     * @param deploymentId
     * @return
     */
    @Override
    public Boolean deleteProcessDefinitionByDeploymentId(String deploymentId) {
        try {
            /**级联删除*/
            repositoryService.deleteDeployment(deploymentId, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 部署zip流程文件
     *
     * @param flowName
     * @param inputStream
     * @return
     */
    @Override
    public DeploymentInfo deploy(String flowName, InputStream inputStream) {
        try {
            Deployment deploy = repositoryService.createDeployment().
                    addZipInputStream(new ZipInputStream(inputStream))
                    .name(flowName)
                    .deploy();
            DeploymentInfo deploymentInfo = new DeploymentInfo();
            deploymentInfo.setId(deploy.getId());
            deploymentInfo.setCategory(deploy.getCategory());
            deploymentInfo.setDeploymentTime(deploy.getDeploymentTime());
            deploymentInfo.setName(deploy.getName());
            deploymentInfo.setTenantId(deploy.getTenantId());
            return deploymentInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询流程图
     *
     * @param processDefinitionInfo
     * @return
     */
    @Override
    public InputStream findImageInputStream(ProcessDefinitionInfo processDefinitionInfo) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(processDefinitionInfo.getDeploymentId(),
                    processDefinitionInfo.getDiagramResourceName());
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 启动流程
     *
     * @param variables
     * @return
     */
    @Override
    public synchronized ProcessInstanceInfo startProcessInstance(FlowDocEntity entity, Map<String, Object> variables, String userId) {
        String processDefinitionKey = entity.getClass().getSimpleName();
        String businessKey = entity.getClass().getSimpleName() + "."
                + entity.getObjId() + "."
                + entity.getDocNo() + "."
                + entity.getSubmitterName() + "."
                + entity.getSubmitter();

        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("userId", userId);
        variables.put("status", DocStatus.AUDITING.getIndex());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

        return processInstanceToInfo(processInstance);
    }


    /**
     * 通过单objId查找流程图
     *
     * @param businessKey
     * @return
     */
    @Override
    public InputStream findFlowBufferedImageByBusinessKey(String businessKey) {


        try {
            //String businessKey = "";
            //查询运行时流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            ProcessDefinition processDefinition = null;
            if (processInstance == null) {
                //查询历史流程实例
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
                if (historicProcessInstance != null) {
                    processDefinition = repositoryService.createProcessDefinitionQuery()
                            .processDefinitionId(historicProcessInstance.getProcessDefinitionId())
                            .singleResult();

                } else {
                    return null;
                }
            } else {
                processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(processInstance.getProcessDefinitionId())
                        .singleResult();
            }
            //如果流程定义不为null 返回流程图输出流
            if (processDefinition != null) {
                InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                        processDefinition.getDiagramResourceName());
                return inputStream;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 通过businessKey 查找流程任务列表
     *
     * @param businessKey
     * @return
     */
    @Override
    public List<TaskInfo> findTaskByBusinessKey(String businessKey) {
        try {
            List<TaskInfo> taskInfoList = new ArrayList<>();
            List<Task> list = taskService.createTaskQuery()
                    .processInstanceBusinessKey(businessKey)
                    .active()
                    .list();

            for (Task task : list) {

                taskInfoList.add(taskToTaskInfo(task));
            }
            return taskInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过taskId获取任务坐标
     *
     * @param taskId
     * @return
     */
    @Override
    public Map<String, Object> findCoordingByTask(String taskId) {
        try {
            //存放坐标
            Map<String, Object> map = new HashMap<String, Object>();
            //使用任务ID，查询任务对象
            Task task = taskService.createTaskQuery()//
                    .taskId(taskId)//使用任务ID查询
                    .singleResult();
            if (task == null) {
                task = taskService.createTaskQuery().processInstanceId(taskId).singleResult();
            }
            if (task == null) {
                return null;
            }

            //获取流程定义的ID
            String processDefinitionId = task.getProcessDefinitionId();
            //获取流程定义的实体对象（对应.bpmn文件中的数据）
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
            //流程实例ID
            String processInstanceId = task.getProcessInstanceId();
            //使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
                    .processInstanceId(processInstanceId)//使用流程实例ID查询
                    .singleResult();
            //获取当前活动的ID
            String activityId = pi.getActivityId();
            //获取当前活动对象
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
            //获取坐标
            map.put("x", activityImpl.getX());
            map.put("y", activityImpl.getY());
            map.put("width", activityImpl.getWidth());
            map.put("height", activityImpl.getHeight());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过taskId获取审批记录列表
     *
     * @param taskId
     * @return
     */
    @Override
    public List<CommentInfo> findCommentByTaskId(String taskId) {
        try {
            List<Comment> list = new ArrayList<Comment>();
            //使用当前的任务ID，查询当前流程对应的历史任务ID
            //使用当前任务ID，获取当前任务对象
            Task task = taskService.createTaskQuery()//
                    .taskId(taskId)//使用任务ID查询
                    .singleResult();
            //获取流程实例ID
            String processInstanceId = task.getProcessInstanceId();

            list = taskService.getProcessInstanceComments(processInstanceId);
            Collections.sort(list, new Comparator<Comment>() {
                @Override
                public int compare(Comment c01, Comment c02) {
                    //比较每个ArrayList的第二个元素
                    if (c01.getTime().getTime() == c02.getTime().getTime()) {
                        return 0;
                    } else if (c01.getTime().getTime() > c02.getTime().getTime()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

            List<CommentInfo> commentInfoList = new ArrayList<>();
            for (Comment comment : list) {
                commentInfoList.add(commentToCommentInfo(comment));
            }
            return commentInfoList;
        } catch (Exception e) {
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
    @Override
    public List<CommentInfo> findCommentByBusinessKey(String businessKey) {
        try {
            List<Comment> list = null;
            List<CommentInfo> commentInfoList = new ArrayList<>();
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();

            if (hpi != null) {
                String processInstanceId = hpi.getId();
                list = taskService.getProcessInstanceComments(processInstanceId);
                Collections.sort(list, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment c01, Comment c02) {
                        //比较每个ArrayList的第二个元素
                        if (c01.getTime().getTime() == c02.getTime().getTime()) {
                            return 0;
                        } else if (c01.getTime().getTime() > c02.getTime().getTime()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
            } else {
                return null;
            }
            if (list != null) {
                for (Comment comment : list) {
                    commentInfoList.add(commentToCommentInfo(comment));
                }
                return commentInfoList;

            } else {
                return commentInfoList;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过logId 查询个人任务列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<TaskInfo> findAllTaskList(String userId) {
        List<Task> personalTasks = this.findPersonalTaskList(userId);
        List<Task> groupTasks = this.findGroupTaskList(userId);

        List<Task> list = new ArrayList<>();

        list.addAll(personalTasks);
        list.addAll(groupTasks);

        //排序
        Collections.sort(list, new Comparator<Task>() {
            @Override
            public int compare(Task task01, Task task02) {
                //比较每个ArrayList的第二个元素
                if (task01.getCreateTime().getTime() == task02.getCreateTime().getTime()) {
                    return 0;
                } else if (task01.getCreateTime().getTime() > task02.getCreateTime().getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        Collections.reverse(list);
        List<TaskInfo> taskInfoList = new ArrayList<>();
        for (Task task : list) {
            taskInfoList.add(taskToTaskInfo(task));
        }

        return taskInfoList;
    }


    /**
     * 使用当前用户名查询正在执行的个人任务表，获取当前个人任务的集合List<Task>
     *
     * @param userId 用户名
     * @return 当前用户的所有个人任务
     */
    @Override
    public List<Task> findPersonalTaskList(String userId) {
        List<Task> list = taskService.createTaskQuery()//
                .taskAssignee(userId)//指定个人任务查询
                .orderByTaskCreateTime().desc()
                .list();

        return list;
    }

    /**
     * 通过流程实例id集合查询
     *
     * @param processInstanceIds
     * @return
     */
    @Override
    public List<ProcessInstanceInfo> findProcessInstanceByIds(Set<String> processInstanceIds) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processInstanceIds(processInstanceIds)
                .list();
        List<ProcessInstanceInfo> processInstanceInfoList = new ArrayList<>();
        for (ProcessInstance processInstance : list) {
            processInstanceInfoList.add(processInstanceToInfo(processInstance));
        }

        return processInstanceInfoList;
    }

    /**
     * 通过流程实例查询
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public ProcessInstance findProcessInstanceById(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return processInstance;
    }


    /**
     * 查询流程定义的信息，对应表（act_re_procdef）
     *
     * @return 所有流程定义集合，包括老版本的流程定义
     */
    @Override
    public List<ProcessDefinitionInfo> findProcessDefinitionList() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
                .orderByProcessDefinitionVersion().desc()//
                .list();
        List<ProcessDefinitionInfo> processDefinitionInfoList = new ArrayList<>();
        for (ProcessDefinition processDefinition : list) {
            processDefinitionInfoList.add(processDefinitionToInfo(processDefinition));
        }


        return processDefinitionInfoList;
    }

    /**
     * 通过流程定义Id查询流程定义
     *
     * @param processDefinitionId
     * @return
     */
    @Override
    public ProcessDefinitionInfo findProcessDefinitionById(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        return processDefinitionToInfo(processDefinition);
    }


    /**
     * 根据任务对象，获取流程实例的FormUrl
     *
     * @param taskInfo
     * @return
     */
    @Override
    public String findFormUrl(TaskInfo taskInfo) {
        String processDefinitionId = taskInfo.getProcessDefinitionId();
        String formUrl = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult().getDescription();
        return formUrl;
    }


    /**
     * 使用任务ID，查找业务ID
     *
     * @param taskId
     * @return 业务ID
     */
    @Override
    public String findBusinessIdByTaskId(String taskId) {
        String buniness_key = findBusinessKeyByTaskId(taskId);
        //5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
        String id = "";
        if (StringUtils.isNotBlank(buniness_key)) {
            //截取字符串，取buniness_key小数点的第2个值
            id = buniness_key.split("\\.")[1];
        }
        return id;

    }

    /**
     * 使用任务ID，找到业务Key
     *
     * @param taskId
     * @return
     */
    @Override
    public String findBusinessKeyByTaskId(String taskId) {
        //1：使用任务ID，查询任务对象Task
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2：使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //4：使用流程实例对象获取BUSINESS_KEY
        String buniness_key = pi.getBusinessKey();
        return buniness_key;
    }

    /**
     * 已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
     */
    @Override
    public List<String> findOutComeListByTaskId(String taskId) {
        //返回存放连线的名称集合
        List<String> list = new ArrayList<String>();
        //1:使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2：获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
        //3：查询ProcessDefinitionEntiy对象
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //获取当前活动的id
        String activityId = pi.getActivityId();
        //4：获取当前的活动
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
        //5：获取当前活动完成之后连线的名称
        List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
        if (pvmList != null && pvmList.size() > 0) {
            for (PvmTransition pvm : pvmList) {
                String name = (String) pvm.getProperty("name");
                if (StringUtils.isNotBlank(name)) {
                    list.add(name);
                } else {
                    list.add("提交");
                }
            }
        }
        return list;
    }

    /**
     * 完成任务
     *
     * @param taskId    任务id
     * @param outcome   审批结果
     * @param comment   审批意见
     * @param userId    用户id
     * @param variables
     * @return
     */
    @Override
    public ProcessInstanceInfo completeTask(String taskId, String outcome, String comment, String userId, Map<String, Object> variables) {
        /**
         * 1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
         */
        //使用任务ID，查询任务对象，获取流程流程实例ID
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();

        if (task == null) {
            System.out.println("任务对象为空，操作异常！");
            return null;
        }

        if (StringUtils.isBlank(task.getAssignee())) {   // 组任务，需要先拾取，然后才能办理
            taskService.claim(taskId, userId);
        }

        //获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        /**
         * 注意：添加批注的时候，由于Activiti底层代码是使用：
         * 		String userId = Authentication.getAuthenticatedUserId();
         CommentEntity comment = new CommentEntity();
         comment.setUserId(userId);
         所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，不过不添加审核人，该字段为null
         所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
         * */
//        CommonHelper.getActiveUser().getUsername();
//        Authentication.setAuthenticatedUserId(CommonHelper.getActiveUser().getUsername());
        Authentication.setAuthenticatedUserId(userId);
        taskService.addComment(taskId, processInstanceId, outcome, comment);

        /**
         * 2：如果连线的名称是“默认提交”，那么就不需要设置，如果不是，就需要设置流程变量
         * 在完成任务之前，设置流程变量，按照连线的名称，去完成任务
         流程变量的名称：outcome
         流程变量的值：连线的名称
         */
//        Map<String, Object> variables = new HashMap<String, Object>();
//        if (outcome != null && !outcome.equals("提交")) {
        if (StringUtils.isNotBlank(outcome)) {
            variables.put("outcome", outcome);
        }

        //3：使用任务ID，完成当前人的个人任务，同时流程变量
        taskService.complete(taskId, variables);
        //4：当任务完成之后，需要指定下一个任务的办理人（使用类）-----已经开发完成

        /**
         * 5：在完成任务之后，判断流程是否结束
         如果流程结束了，更新请假单表的状态从1变成2（审核中-->审核完成）
         */
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        return processInstanceToInfo(processInstance);
    }


    /**
     * 根据BusinessKey查找ProcessInstance
     *
     * @param businessKey
     * @return
     */
    @Override
    public ProcessInstanceInfo findProcessInstanceByBusinessKey(String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        return processInstanceToInfo(processInstance);
    }

    @Override
    public HistoricProcessInstanceInfo findHistoricProcessInstanceByBusinessKey(String businessKey) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        return historicProcessInstanceToInfo(historicProcessInstance);
    }


    /**
     * 使用流程实例ID，找到业务Key
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public String findBusinessKeyByProcessInstanceId(String processInstanceId) {
        //3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //4：使用流程实例对象获取BUSINESS_KEY
        String buniness_key = pi.getBusinessKey();
        return buniness_key;
    }

    /**
     * 获取等待执行的任务
     *
     * @return
     */
    @Override
    public List<TaskInfo> findWaitingTasks() {
        List<Task> tasks = taskService.createTaskQuery().list();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        for (Task task : tasks) {
            taskInfoList.add(taskToTaskInfo(task));
        }
        return taskInfoList;
    }


    /**
     * 保存审批记录
     *
     * @param commentItem
     * @return
     */
    @Override
    public CommentEntity saveComment(CommentEntity commentItem) {
        try {
            return commentRepository.save(commentItem);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过logid 查询已完成历史任务
     *
     * @param logId
     * @return
     */
    @Override
    public List<HistoricProcessInstanceInfo> findHistoricTaskByLogId(String logId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(logId)
                .processFinished()
                .list();
        Set<String> processInstanceIds = new HashSet<>();

        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            processInstanceIds.add(historicTaskInstance.getProcessInstanceId());
        }
        List<HistoricProcessInstance> list = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceIds(processInstanceIds).orderByProcessInstanceStartTime().desc()
                .list();
        List<HistoricProcessInstanceInfo> infoList = new ArrayList<>();
        for (HistoricProcessInstance historicProcessInstance : list) {
            infoList.add(historicProcessInstanceToInfo(historicProcessInstance));
        }
        return infoList;
    }

    /**
     * 通过logId获取已处理任务
     *
     * @param logId
     * @return
     */
    @Override
    public List<HistoricTaskInstanceInfo> findHandleTaskByLogId(String logId) {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(logId)
                .finished()
                .list();
        Map<String, HistoricTaskInstance> mapHistoricTask = new HashMap<>();
        //历史处理任务不为null 过滤历史任务
        if (list != null) {
            for (HistoricTaskInstance historicTaskInstance : list) {
                //如果不存在
                if (mapHistoricTask.get(historicTaskInstance.getProcessInstanceId()) == null) {
                    mapHistoricTask.put(historicTaskInstance.getProcessInstanceId(), historicTaskInstance);
                } else {
                    HistoricTaskInstance oldhistoricTask = mapHistoricTask.get(historicTaskInstance.getProcessInstanceId());

                    if (historicTaskInstance.getEndTime().getTime() > oldhistoricTask.getEndTime().getTime()) {
                        mapHistoricTask.remove(oldhistoricTask.getProcessInstanceId());
                        mapHistoricTask.put(historicTaskInstance.getProcessInstanceId(), historicTaskInstance);
                    }
                }
            }
        }
        List<HistoricTaskInstanceInfo> historicTaskInstanceInfoList = new ArrayList<>();

        for (Map.Entry<String, HistoricTaskInstance> Entry : mapHistoricTask.entrySet()) {
            HistoricTaskInstance historicTaskInstance = Entry.getValue();
            //获取当前实例所有待受理的任务
            List<Task> taskList = taskService.createTaskQuery()
                    .processDefinitionId(historicTaskInstance.getProcessDefinitionId())
                    .processInstanceId(historicTaskInstance.getProcessInstanceId()).list();

            //通过当前任务节点获取后续任务节点
            List<TaskDefinition> nextTasks = getNextTaskIds(historicTaskInstance.getProcessDefinitionId(), historicTaskInstance.getTaskDefinitionKey());
            for (Task task : taskList) {
                //如果待受理任务在当前人就过滤掉
                //if(!task.getTaskDefinitionKey().equals(historicTaskInstance.getTaskDefinitionKey())){
                //    historicTaskInstanceInfoList.add(historicTaskInstanceToInfo(historicTaskInstance));
                //}else {
                if (nextTasks != null && nextTasks.size() > 0) {
                    //如果下一个任务是待受理任务可以撤回
                    for (TaskDefinition taskDefinition : nextTasks) {
                        String taskKey = taskDefinition.getKey();//任务节点id bpmn.xml （userTask的id）
                        String key = task.getTaskDefinitionKey();
                        if (taskKey.equals(key)) {
                            historicTaskInstanceInfoList.add(historicTaskInstanceToInfo(historicTaskInstance));
                        }
                    }
                    //}
                }
            }

        }


        //排序
        Collections.sort(historicTaskInstanceInfoList, new Comparator<HistoricTaskInstanceInfo>() {
            @Override
            public int compare(HistoricTaskInstanceInfo task01, HistoricTaskInstanceInfo task02) {
                //比较每个ArrayList的第二个元素
                if (task01.getCreateTime().getTime() == task02.getCreateTime().getTime()) {
                    return 0;
                } else if (task01.getCreateTime().getTime() > task02.getCreateTime().getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        Collections.reverse(historicTaskInstanceInfoList);


        return historicTaskInstanceInfoList;
    }


    /**
     * 使用流程实例ID，查找业务ID
     *
     * @param processInstanceId
     * @return 业务ID
     */
    @Override
    public String findBusinessIdByProcessInstanceId(String processInstanceId) {
        String buniness_key = findBusinessKeyByProcessInstanceId(processInstanceId);
        //5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
        String id = "";
        if (StringUtils.isNotBlank(buniness_key)) {
            //截取字符串，取buniness_key小数点的第2个值
            id = buniness_key.split("\\.")[1];
        }
        return id;
    }


    /**
     * 使用当前用户名查询正在执行的组任务表，获取当前组任务的集合List<Task>
     *
     * @param userId
     * @return 当前用户的所有组任务
     */
    @Override
    public List<Task> findGroupTaskList(String userId) {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .orderByTaskCreateTime().desc()
                .list();
        return list;
    }


    /**
     * 撤回任务
     *
     * @return
     */
    @Override
    public Boolean callBackTask(String historicTaskInstanceInfoId, String userId) {

        HistoricTaskInstance historicTaskInstanceInfo = historyService
                .createHistoricTaskInstanceQuery()
                .taskId(historicTaskInstanceInfoId)
                .singleResult();

        //通过当前任务节点获取后续任务节点
        List<TaskDefinition> nextTasks = getNextTaskIds(historicTaskInstanceInfo.getProcessDefinitionId(), historicTaskInstanceInfo.getTaskDefinitionKey());

        //判断后续任务节点是否都处在处理状态
        List<Task> nextTaskList = isInRuntimeWhereTask(historicTaskInstanceInfo.getProcessDefinitionId(), historicTaskInstanceInfo.getProcessInstanceId(), historicTaskInstanceInfo.getTaskDefinitionKey(), nextTasks);


        String service = BeanUtils.getServiceByProcessDefinitionId(historicTaskInstanceInfo.getProcessDefinitionId());
        this.setBaseService((BaseService) applicationContext.getBean(service));
        //重新生成当前任务节点的待办任务
        //Boolean Success = jumpEndActivity(nextTaskList, historicTaskInstanceInfo);
        //撤回成功更改单据状态
        //if (Success) {
        //    //String service = BeanUtils.getServiceByProcessDefinitionId(historicTaskInstanceInfo.getProcessDefinitionId());
        //    //this.setBaseService((BaseService) applicationContext.getBean(service));
        //    //String businessId = findBusinessIdByProcessInstanceId(historicTaskInstanceInfo.getProcessInstanceId());
        //    //FlowDocEntity Entity = (FlowDocEntity) baseService.getRepository().findOne(businessId);
        //    //Entity.setStatus(DocStatus.WITHDRAW.getIndex());
        //    //baseService.getRepository().save(Entity);
        //    return true;
        //}

        return jumpEndActivity(nextTaskList, historicTaskInstanceInfo, userId);

    }


    /**
     * 中止正在运行的流程实例
     *
     * @param processInstanceId
     */
    @Override
    public Boolean deleteProcessInstance(String processInstanceId) {
        try {
            runtimeService.deleteProcessInstance(processInstanceId, "作废单据");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除流程和记录
     *
     * @param processInstanceId
     */
    @Override
    public Boolean deleteHistoricProcessInstance(String processInstanceId) {
        try {
            historyService.deleteHistoricProcessInstance(processInstanceId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 流程实例id查找审批日记
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<CommentEntity> findCommentByProcessInstanceId(String processInstanceId) {
        return commentRepository.findAllByProcessInstanceId(processInstanceId);
    }


    /**
     * 获取当前节点后续的任务节点
     *
     * @param processDefinitionId 流程定义id
     * @param activitiId          任务节点id bpmn.xml （userTask的id）
     * @return
     */
    private List<TaskDefinition> getNextTaskIds(String processDefinitionId, String activitiId) {

        // 2、然后根据当前任务获取当前流程的流程定义，然后根据流程定义获得所有的节点：
        ProcessDefinitionEntity ProcessDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        List<ActivityImpl> activitiList = ProcessDefinition.getActivities(); // rs是指RepositoryService的实例

        //String activitiId = "usertask1";//任务节点id bpmn.xml

        // 4、然后循环activitiList
        // 并判断出当前任务所处节点，然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，然后根据路径获得下一个节点实例：
        List<TaskDefinition> nextTasks = new ArrayList<>();
        for (ActivityImpl activityImpl : activitiList) {

            String id = activityImpl.getId();
            if (activitiId.equals(id)) {
                System.out.println("当前任务：" + activityImpl.getProperty("name")); // 输出某个节点的某种属性
                List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();// 获取从某个节点出来的所有线路
                for (PvmTransition tr : outTransitions) {
                    //PvmActivity ac = tr.getDestination(); // 获取线路的终点节点
                    //System.out.println("下一步任务任务：" + ac.getProperty("name"));
                    nextTasks = nextTaskDefinition(activityImpl, activitiId);

                }
                break;
            }
        }
        return nextTasks;
    }


    private String nextTaskType(String processDefinitionId, String activitiId) {
        // 2、然后根据当前任务获取当前流程的流程定义，然后根据流程定义获得所有的节点：
        ProcessDefinitionEntity ProcessDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        List<ActivityImpl> activitiList = ProcessDefinition.getActivities(); // rs是指RepositoryService的实例
        for (ActivityImpl activityImpl : activitiList) {

            String id = activityImpl.getId();
            if (activitiId.equals(id)) {
                System.out.println("当前任务：" + activityImpl.getProperty("name")); // 输出某个节点的某种属性
                List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();// 获取从某个节点出来的所有线路
                for (PvmTransition tr : outTransitions) {
                    //PvmActivity ac = tr.getDestination(); // 获取线路的终点节点
                    //System.out.println("下一步任务任务：" + ac.getProperty("name"));
                    PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                    return (String) ac.getProperty("type");

                }
                break;
            }
        }
        return "";
    }


    /**
     * 获取所有下一节点集合
     *
     * @param activityImpl
     * @param activityId
     * @return
     */
    private List<TaskDefinition> nextTaskDefinition(ActivityImpl activityImpl, String activityId) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<>();//所有的任务实例
        List<TaskDefinition> nextTaskDefinition = new ArrayList<>();//逐个获取的任务实例
        TaskDefinition taskDefinition = null;
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            taskDefinitionList.add(taskDefinition);
        } else {
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                //如果是互斥网关或者是并行网关
                if ("exclusiveGateway".equals(ac.getProperty("type")) || "parallelGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    if (outTransitionsTemp.size() == 1) {
                        nextTaskDefinition = nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId);
                        taskDefinitionList.addAll(nextTaskDefinition);
                    } else if (outTransitionsTemp.size() > 1) {
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            nextTaskDefinition = nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId);
                            taskDefinitionList.addAll(nextTaskDefinition);
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                    taskDefinitionList.add(taskDefinition);
                } else {
                    System.out.println((String) ac.getProperty("type"));
                }
            }
        }
        return taskDefinitionList;
    }

    /**
     * 下一节点的任务（所有任务）是否还处在待处理状态
     * 比配成功，返回下一节点的待办任务
     * act_ru_task
     *
     * @return
     */
    private List<Task> isInRuntimeWhereTask(String processDefinitionId, String processInstanceId, String activitiId, List<TaskDefinition> nextTasks) {

        if (nextTasks == null || !(nextTasks.size() > 0)) {
            return null;
        }

        //获取当前实例所有待受理的任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionId(processDefinitionId)
                .processInstanceId(processInstanceId).list();


        int count = 0;
        List<Task> nextTaskList = new ArrayList<>();
        for (TaskDefinition taskDefinition : nextTasks) {

            String taskKey = taskDefinition.getKey();//任务节点id bpmn.xml （userTask的id）

            for (Task task : taskList) {
                String key = task.getTaskDefinitionKey();
                if (taskKey.equals(key)) {
                    nextTaskList.add(task);
                    count++;
                }
            }
        }

        String nextTaskType = nextTaskType(processDefinitionId, activitiId);
        if ("userTask".equals(nextTaskType)) {
            return nextTaskList;
        } else if ("exclusiveGateway".equals(nextTaskType)) {
            if (nextTasks.size() > 0) {//所有节点都处在待处理状态（即可以执行撤回）
                return nextTaskList;
            } else {
                return null;//如果后续节点待某个节点已经执行，那么就不能执行撤回
            }
        } else if ("parallelGateway".equals(nextTaskType)) {
            if (nextTasks.size() == count) {//所有节点都处在待处理状态（即可以执行撤回）
                return nextTaskList;
            } else {
                return null;//如果后续节点待某个节点已经执行，那么就不能执行撤回
            }
        }

        return null;
    }


    /**
     * 第一种自由跳转的方式:
     * 这种方式是通过 重写命令  ，推荐这种方式进行实现(这种方式的跳转，最后可以通过taskDeleteReason 来进行查询 )
     */
    public Boolean jumpEndActivity(List<Task> taskList, HistoricTaskInstance historicTaskInstanceInfo, String userId) {
        //当前节点
        //ActivityImpl currentActivityImpl = getActivityImpl(processDefinitionId,);
        //目标节点
        //ActivityImpl targetActivity = getActivityImpl("shenchajigou");

        if (taskList == null || taskList.size() == 0) {
            return false;
        }

        try {
            ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(new Command<Object>() {
                @Override
                public Object execute(CommandContext commandContext) {
                    ExecutionEntity execution = null;
                    Task tt = null;
                    for (Task task : taskList) {
                        //当前正在执行的流程实例Id
                        tt = task;
                        execution = commandContext.getExecutionEntityManager().findExecutionById(task.getExecutionId());
                        execution.destroyScope("jump");  //
                    }

                    String businessId = findBusinessIdByProcessInstanceId(historicTaskInstanceInfo.getProcessInstanceId());
                    FlowDocEntity Entity = (FlowDocEntity) baseService.getRepository().findOne(businessId);
                    Entity.setStatus(DocStatus.WITHDRAW.getIndex());
                    baseService.getRepository().save(Entity);

                    ProcessDefinitionImpl processDefinition = execution.getProcessDefinition();
                    ActivityImpl findActivity = processDefinition.findActivity(historicTaskInstanceInfo.getTaskDefinitionKey());
                    //Map<String, Object> variables = new HashMap<>();
                    //variables.put("status", DocStatus.WITHDRAW.getIndex());
                    //findActivity.setVariables(variables);
                    execution.setVariable("status", DocStatus.WITHDRAW.getIndex());
                    execution.executeActivity(findActivity);

//                    Authentication.setAuthenticatedUserId(tt.getAssignee());
                    Authentication.setAuthenticatedUserId(userId);
//                    taskService.addComment(taskId, processInstanceId, outcome, comment);
                    taskService.addComment(null, execution.getProcessInstanceId(), "撤回", "撤回");

                    return execution;
                }

            });
            System.out.println("撤销完成");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 转换comment类型
     *
     * @param comment
     * @return
     */
    private CommentInfo commentToCommentInfo(Comment comment) {
        if (comment != null) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setId(comment.getId());
            commentInfo.setUserId(comment.getUserId());
            commentInfo.setTaskId(comment.getTaskId());
            commentInfo.setProcessInstanceId(comment.getProcessInstanceId());
            commentInfo.setType(comment.getType());
            commentInfo.setTime(comment.getTime());
            commentInfo.setFullMessage(comment.getFullMessage());
            return commentInfo;
        } else {
            return null;
        }

    }

    /**
     * 转换Task类型
     *
     * @param task
     * @return
     */
    private TaskInfo taskToTaskInfo(Task task) {
        if (task != null) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setId(task.getId());
            taskInfo.setName(task.getName());
            taskInfo.setDescription(task.getDescription());
            taskInfo.setPriority(task.getPriority());
            taskInfo.setOwner(task.getOwner());
            taskInfo.setAssignee(task.getAssignee());
            taskInfo.setProcessInstanceId(task.getProcessInstanceId());
            taskInfo.setExecutionId(task.getExecutionId());
            taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
            taskInfo.setCreateTime(task.getCreateTime());
            taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            taskInfo.setDueDate(task.getDueDate());
            taskInfo.setCategory(task.getCategory());
            taskInfo.setParentTaskId(task.getParentTaskId());
            taskInfo.setTenantId(task.getTenantId());
            taskInfo.setFormKey(task.getFormKey());
            taskInfo.setTaskLocalVariables(task.getTaskLocalVariables());
            taskInfo.setProcessVariables(task.getProcessVariables());
            return taskInfo;
        } else {
            return null;
        }

    }

    /**
     * processInstance转换
     *
     * @param processInstance
     * @return
     */
    private ProcessInstanceInfo processInstanceToInfo(ProcessInstance processInstance) {
        if (processInstance != null) {
            ProcessInstanceInfo processInstanceInfo = ProcessInstanceInfo.ProcessInstanceInfoBuilder.aProcessInstanceInfo()
                    .withDeploymentId(processInstance.getDeploymentId())
                    .withProcessDefinitionName(processInstance.getProcessDefinitionName())
                    .withProcessDefinitionKey(processInstance.getProcessDefinitionKey())
                    .withProcessDefinitionVersion(processInstance.getProcessDefinitionVersion())
                    .withDeploymentId(processInstance.getDeploymentId())
                    .withBusinessKey(processInstance.getBusinessKey())
                    .withIsSuspended(processInstance.isSuspended())
                    .withProcessVariables(processInstance.getProcessVariables())
                    .withTenantId(processInstance.getTenantId())
                    .withName(processInstance.getName())
                    .withDescription(processInstance.getDescription())
                    .withLocalizedName(processInstance.getLocalizedName())
                    .withLocalizedDescription(processInstance.getLocalizedDescription())
                    .withId(processInstance.getId())
                    .build();
            return processInstanceInfo;
        } else {
            return null;
        }
    }

    /**
     * processDefinition 转换info
     *
     * @param processDefinition
     * @return
     */
    private ProcessDefinitionInfo processDefinitionToInfo(ProcessDefinition processDefinition) {
        if (processDefinition != null) {
            ProcessDefinitionInfo processDefinitionInfo = new ProcessDefinitionInfo();
            processDefinitionInfo.setId(processDefinition.getId());
            processDefinitionInfo.setCategory(processDefinition.getCategory());
            processDefinitionInfo.setName(processDefinition.getName());
            processDefinitionInfo.setKey(processDefinition.getKey());
            processDefinitionInfo.setDescription(processDefinition.getDescription());
            processDefinitionInfo.setVersion(processDefinition.getVersion());
            processDefinitionInfo.setResourceName(processDefinition.getResourceName());
            processDefinitionInfo.setDeploymentId(processDefinition.getDeploymentId());
            processDefinitionInfo.setDiagramResourceName(processDefinition.getDiagramResourceName());
            return processDefinitionInfo;
        } else {
            return null;
        }

    }


    /**
     * historicProcessInstance转info
     *
     * @param historicProcessInstance
     * @return
     */
    private HistoricProcessInstanceInfo historicProcessInstanceToInfo(HistoricProcessInstance historicProcessInstance) {
        if (historicProcessInstance != null) {
            HistoricProcessInstanceInfo historicProcessInstanceInfo = new HistoricProcessInstanceInfo();
            historicProcessInstanceInfo.setId(historicProcessInstance.getId());
            historicProcessInstanceInfo.setBusinessKey(historicProcessInstance.getBusinessKey());
            historicProcessInstanceInfo.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
            historicProcessInstanceInfo.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            historicProcessInstanceInfo.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
            historicProcessInstanceInfo.setProcessDefinitionVersion(historicProcessInstance.getProcessDefinitionVersion());
            historicProcessInstanceInfo.setDeploymentId(historicProcessInstance.getDeploymentId());
            historicProcessInstanceInfo.setStartTime(historicProcessInstance.getStartTime());
            historicProcessInstanceInfo.setEndTime(historicProcessInstance.getEndTime());
            historicProcessInstanceInfo.setDurationInMillis(historicProcessInstance.getDurationInMillis());
            historicProcessInstanceInfo.setSuperProcessInstanceId(historicProcessInstance.getSuperProcessInstanceId());
            historicProcessInstanceInfo.setStartUserId(historicProcessInstance.getStartUserId());
            historicProcessInstanceInfo.setStartActivityId(historicProcessInstance.getStartActivityId());
            historicProcessInstanceInfo.setDeleteReason(historicProcessInstance.getDeleteReason());
            historicProcessInstanceInfo.setSuperProcessInstanceId(historicProcessInstance.getSuperProcessInstanceId());
            historicProcessInstanceInfo.setTenantId(historicProcessInstance.getTenantId());
            historicProcessInstanceInfo.setName(historicProcessInstance.getName());
            historicProcessInstanceInfo.setDescription(historicProcessInstance.getDescription());
            historicProcessInstanceInfo.setProcessVariables(historicProcessInstance.getProcessVariables());
            return historicProcessInstanceInfo;
        } else {
            return null;
        }
    }

    /**
     * historicTaskInstance转info
     *
     * @param historicTaskInstance
     * @return
     */
    private HistoricTaskInstanceInfo historicTaskInstanceToInfo(HistoricTaskInstance historicTaskInstance) {
        if (historicTaskInstance != null) {
            HistoricTaskInstanceInfo historicTaskInstanceInfo = HistoricTaskInstanceInfo.HistoricTaskInstanceInfoBuilder.aHistoricTaskInstanceInfo()
                    .withId(historicTaskInstance.getId())
                    .withDeleteReason(historicTaskInstance.getDeleteReason())
                    .withStartTime(historicTaskInstance.getStartTime())
                    .withName(historicTaskInstance.getName())
                    .withEndTime(historicTaskInstance.getEndTime())
                    .withDescription(historicTaskInstance.getDescription())
                    .withDurationInMillis(historicTaskInstance.getDurationInMillis())
                    .withPriority(historicTaskInstance.getPriority())
                    .withOwner(historicTaskInstance.getOwner())
                    .withAssignee(historicTaskInstance.getAssignee())
                    .withProcessInstanceId(historicTaskInstance.getProcessInstanceId())
                    .withWorkTimeInMillis(historicTaskInstance.getWorkTimeInMillis())
                    .withExecutionId(historicTaskInstance.getExecutionId())
                    .withProcessDefinitionId(historicTaskInstance.getProcessDefinitionId())
                    .withClaimTime(historicTaskInstance.getClaimTime())
                    .withCreateTime(historicTaskInstance.getCreateTime())
                    .withTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey())
                    .withDueDate(historicTaskInstance.getDueDate())
                    .withCategory(historicTaskInstance.getCategory())
                    .withParentTaskId(historicTaskInstance.getParentTaskId())
                    .withTenantId(historicTaskInstance.getTenantId())
                    .withFormKey(historicTaskInstance.getFormKey())
                    .withTaskLocalVariables(historicTaskInstance.getTaskLocalVariables())
                    .withProcessVariables(historicTaskInstance.getProcessVariables())
                    .build();

            return historicTaskInstanceInfo;
        }
        return null;
    }


}
