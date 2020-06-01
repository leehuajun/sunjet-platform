package com.sunjet.frontend.service.flow;

import com.sunjet.dto.asms.flow.*;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Created by SUNJET_WS on 2017/9/5.
 */
@Slf4j
@Service("processService")
public class ProcessService {

    @Autowired
    private RestClient restClient;

    /**
     * 获取最新的流程定义版本
     *
     * @return
     */

    public List<ProcessDefinitionInfo> findProcessDefinitionLastVersionList() {

        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<ProcessDefinitionInfo> list = restClient.findAll("/process/findProcessDefinitionLastVersionList", requestEntity, new ParameterizedTypeReference<List<ProcessDefinitionInfo>>() {
            });
            log.info("ProcessServicelmpl:findProcessDefinitionLastVersionList:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 查找部署对象
     *
     * @return
     */

    public List<DeploymentInfo> findDeploymentList() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<DeploymentInfo> list = restClient.findAll("/process/findDeploymentList", requestEntity, new ParameterizedTypeReference<List<DeploymentInfo>>() {
            });
            log.info("ProcessServicelmpl:findDeploymentList:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除部署对象
     *
     * @param id
     * @return
     */

    public Boolean deleteProcessDefinitionByDeploymentId(String id) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(id, null);
            responseEntity = restClient.delete("/process/deleteProcessDefinitionByDeploymentId", requestEntity, Boolean.class);
            log.info("ProcessServicelmpl:deleteProcessDefinitionByDeploymentId:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 部署流程
     *
     * @param fileName
     * @param realFilePath
     * @return
     */

    public DeploymentInfo deploy(String fileName, String realFilePath) {

        FileSystemResource resource = new FileSystemResource(new File(realFilePath));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        param.add("fileName", fileName);

        try {
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, null);
            ResponseEntity<DeploymentInfo> responseEntity = restClient.post("/process/deploy", httpEntity, DeploymentInfo.class);
            log.info("ProcessServicelmpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ProcessServicelmpl:deploy:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查看流程图片
     *
     * @param processDefinition
     * @return
     */

    public BufferedImage findImageInputStream(ProcessDefinitionInfo processDefinition) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(processDefinition, null);
            responseEntity = restClient.post("/process/findImageInputStream", requestEntity, byte[].class);
            log.info("ProcessServicelmpl:findImageInputStream:success");
            BufferedImage read = ImageIO.read(IOUtils.byte2Input(responseEntity.getBody()));
            return read;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过单据BusinessKey 查找流程图
     *
     * @param businessKey
     * @return
     */

    public BufferedImage findFlowBufferedImageByBusinessKey(String businessKey) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(businessKey, null);
            responseEntity = restClient.post("/process/findFlowBufferedImageByBusinessKey", requestEntity, byte[].class);
            log.info("ProcessServicelmpl:findFlowBufferedImageByBusinessKey:success");
            if (responseEntity.getBody() != null) {
                BufferedImage read = ImageIO.read(IOUtils.byte2Input(responseEntity.getBody()));
                return read;
            } else {
                return null;
            }

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

    public List<CommentInfo> findCommentByBusinessKey(String businessKey) {
        ResponseEntity<CommentInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(businessKey, null);
            List<CommentInfo> list = restClient.findAll("/process/findCommentByBusinessKey", requestEntity, new ParameterizedTypeReference<List<CommentInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 用过businessKey查任务
     *
     * @param businessKey
     * @return
     */

    public List<TaskInfo> findTaskByBusinessKey(String businessKey) {
        ResponseEntity<TaskInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(businessKey, null);
            List<TaskInfo> list = restClient.findAll("/process/findTaskByBusinessKey", requestEntity, new ParameterizedTypeReference<List<TaskInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过任务id查找任务坐标
     *
     * @param taskId
     * @return
     */

    public Map<String, Object> findCoordingByTask(String taskId) {

        ResponseEntity<Map> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(taskId, null);
            responseEntity = restClient.get("/process/findCoordingByTask", requestEntity, Map.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过taskId 获取审批记录
     *
     * @param takid
     * @return
     */

    public List<CommentInfo> findCommentByTaskId(String takid) {
        ResponseEntity<TaskInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(takid, null);
            List<CommentInfo> list = restClient.findAll("/process/findCommentByTaskId", requestEntity, new ParameterizedTypeReference<List<CommentInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过用户logid查询任务列表
     *
     * @param logId
     * @return
     */

    public List<TaskInfo> findAllTaskList(String logId) {
        ResponseEntity<TaskInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(logId, null);
            List<TaskInfo> list = restClient.findAll("/process/findAllTaskList", requestEntity, new ParameterizedTypeReference<List<TaskInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过流程实例id 查询流程实例
     *
     * @param processInstanceIds
     * @return
     */

    public List<ProcessInstanceInfo> findProcessInstanceByIds(Set<String> processInstanceIds) {
        ResponseEntity<ProcessInstanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(processInstanceIds, null);
            List<ProcessInstanceInfo> list = restClient.findAll("/process/findProcessInstanceByIds", requestEntity, new ParameterizedTypeReference<List<ProcessInstanceInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询流程定义的信息，对应表（act_re_procdef）
     *
     * @return 所有流程定义集合，包括老版本的流程定义
     */

    public List<ProcessDefinitionInfo> findProcessDefinitionList() {
        ResponseEntity<ProcessDefinitionInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<ProcessDefinitionInfo> list = restClient.findAll("/process/findProcessDefinitionList", requestEntity, new ParameterizedTypeReference<List<ProcessDefinitionInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据流程部署Id 查询流程
     *
     * @param processDefinitionId
     * @return
     */

    public ProcessDefinitionInfo findProcessDefinitionById(String processDefinitionId) {
        ResponseEntity<ProcessDefinitionInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(processDefinitionId, null);
            responseEntity = restClient.get("/process/findProcessDefinitionById", requestEntity, ProcessDefinitionInfo.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据任务对象，获取流程实例的FormUrl
     *
     * @param task
     * @return
     */

    public String findFormUrl(TaskInfo task) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(task, null);
            responseEntity = restClient.get("/process/findFormUrl", requestEntity, String.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 使用任务ID，查找业务ID
     *
     * @param taskId
     * @return 业务ID
     */

    public String findBusinessIdByTaskId(String taskId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(taskId, null);
            responseEntity = restClient.get("/process/findBusinessIdByTaskId", requestEntity, String.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
     *
     * @param taskId
     * @return
     */

    public List<String> findOutComeListByTaskId(String taskId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(taskId, null);
            List<String> list = restClient.findAll("/process/findOutComeListByTaskId", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            Collections.sort(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 审批任务
     *
     * @param taskId
     * @param outcome
     * @param comment
     * @param userId
     * @param variables
     * @return
     */

    public ProcessInstanceInfo completeTask(String taskId, String outcome, String comment, String userId, Map<String, Object> variables) {
        ResponseEntity<ProcessInstanceInfo> responseEntity = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("taskId", taskId);
            map.put("outcome", outcome);
            map.put("comment", comment);
            map.put("userId", userId);
            map.put("variables", variables);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.get("/process/completeTask", requestEntity, ProcessInstanceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据BusinessKey查找ProcessInstance
     *
     * @param businessKey
     * @return
     */

    public ProcessInstanceInfo findProcessInstanceByBusinessKey(String businessKey) {
        ResponseEntity<ProcessInstanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(businessKey, null);
            responseEntity = restClient.get("/process/findProcessInstanceByBusinessKey", requestEntity, ProcessInstanceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据BusinessKey查找HistoricProcessInstance
     *
     * @param businessKey
     * @return
     */

    public HistoricProcessInstanceInfo findHistoricProcessInstanceByBusinessKey(String businessKey) {
        ResponseEntity<HistoricProcessInstanceInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(businessKey, null);
            responseEntity = restClient.get("/process/findHistoricProcessInstanceByBusinessKey", requestEntity, HistoricProcessInstanceInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查找等待处理任务
     *
     * @return
     */

    public List<TaskInfo> findWaitingTasks() {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<TaskInfo> list = restClient.findAll("/process/findWaitingTasks", requestEntity, new ParameterizedTypeReference<List<TaskInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存审批日记
     *
     * @param commentEntity
     */

    public CommentItem saveComment(CommentItem commentEntity) {

        ResponseEntity<CommentItem> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(commentEntity, null);
            responseEntity = restClient.get("/process/saveComment", requestEntity, CommentItem.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过logId 查找历史已完成任务
     *
     * @param logId
     * @return
     */

    public List<HistoricProcessInstanceInfo> findHistoricTaskByLogId(String logId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(logId, null);
            List<HistoricProcessInstanceInfo> list = restClient.findAll("/process/findHistoricTaskByLogId", requestEntity, new ParameterizedTypeReference<List<HistoricProcessInstanceInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取已处理任务
     *
     * @param logId
     */

    public List<HistoricTaskInstanceInfo> findHandleTaskByLogId(String logId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(logId, null);
            List<HistoricTaskInstanceInfo> list = restClient.findAll("/process/findHandleTaskByLogId", requestEntity, new ParameterizedTypeReference<List<HistoricTaskInstanceInfo>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 撤销任务
     *
     * @param historicTaskInstanceInfoId
     * @param userId
     * @return
     */

    public Boolean callBackTask(String historicTaskInstanceInfoId, String userId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("historicTaskInstanceInfoId", historicTaskInstanceInfoId);
            map.put("userId", userId);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/process/callBackTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<CommentItem> findCommentByProcessInstanceId(String processInstanceId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(processInstanceId, null);
            List<CommentItem> list = restClient.findAll("/process/findCommentByProcessInstanceId", requestEntity, new ParameterizedTypeReference<List<CommentItem>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
