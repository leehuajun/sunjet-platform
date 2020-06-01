package com.sunjet.frontend.vm.flow;

import com.sunjet.dto.asms.flow.ProcessDefinitionInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.frontend.service.flow.ProcessService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/9/6.
 */
public class ImageViewerVM extends FormVM {

    @WireVariable
    private ProcessService processService;

    @Getter
    @Setter
    private BufferedImage flowBufferedImage;
    @Getter
    @Setter
    private Map<String, Object> coording;
    @Getter
    @Setter
    private Boolean show = true;

    @Init(superclass = true)
    public void init() {
        TaskInfo task = (TaskInfo) Executions.getCurrent().getArg().get("task");
        ProcessDefinitionInfo processDefinition = (ProcessDefinitionInfo) Executions.getCurrent().getArg().get("processDefinition");

        if (processDefinition == null && task == null) {
            ZkUtils.showError("流程定义实体和任务实体不能同时为空，请与管理员联系！", "系统错误");
            return;
        }
        if (processDefinition == null) {
            processDefinition = processService.findProcessDefinitionById(task.getProcessDefinitionId());
        }

        this.flowBufferedImage = processService.findImageInputStream(processDefinition);


        if (task != null) {
            Map<String, Object> tmpCoording = processService.findCoordingByTask(task.getId());
//            添加红色的边框为: height  width  x+3  y+31
//            tmpCoording.replace("x",(Integer)tmpCoording.get("x") + 3);
//            tmpCoording.replace("y",(Integer)tmpCoording.get("y") + 31);
            if (tmpCoording == null) {
                tmpCoording = processService.findCoordingByTask(task.getProcessInstanceId());
            }
            coording = tmpCoording;
            System.out.println("x:" + coording.get("x"));
            System.out.println("y:" + coording.get("y"));
            System.out.println("width:" + coording.get("width"));
            System.out.println("height:" + coording.get("height"));
        } else {
            coording = new HashMap<>();
            coording.put("x", 0);
            coording.put("y", 0);
            coording.put("width", 0);
            coording.put("height", 0);
            show = false;
        }


    }


}
