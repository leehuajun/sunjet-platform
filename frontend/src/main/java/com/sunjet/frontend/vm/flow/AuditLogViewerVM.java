package com.sunjet.frontend.vm.flow;

import com.sunjet.dto.asms.flow.CommentInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.flow.ProcessService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class AuditLogViewerVM extends FormVM {
    @WireVariable
    private ProcessService processService;

    //    private String imagePath;
    @Getter
    @Setter
    private BufferedImage flowBufferedImage;
    @Getter
    @Setter
    private List<CommentInfo> comments = new ArrayList<>();
    @Getter
    @Setter
    private Map<String, Object> coording;
    @Setter
    @Getter
    private Boolean show = true;


    @Init(superclass = true)
    public void init() throws IOException {

        try {
            FlowDocInfo flowDocInfo = (FlowDocInfo) Executions.getCurrent().getArg().get("entity");
            String businessKey01 = StringUtils.replace(flowDocInfo.getClass().getSimpleName(), "Info", "Entity")
                    + "." + flowDocInfo.getObjId()
                    + "." + flowDocInfo.getDocNo()
                    + "." + flowDocInfo.getSubmitterName();
            String businessKey02 = StringUtils.replace(flowDocInfo.getClass().getSimpleName(), "Info", "Entity")
                    + "." + flowDocInfo.getObjId()
                    + "." + flowDocInfo.getDocNo()
                    + "." + flowDocInfo.getSubmitterName()
                    + "." + flowDocInfo.getSubmitter();
            String businessKey = "";
            BufferedImage bufferedImage = processService.findFlowBufferedImageByBusinessKey(businessKey01);
            if (bufferedImage == null) {
                businessKey = businessKey02;
                this.flowBufferedImage = processService.findFlowBufferedImageByBusinessKey(businessKey02);
            } else {
                businessKey = businessKey01;
                this.flowBufferedImage = bufferedImage;
            }
            if (this.flowBufferedImage == null) {
                ZkUtils.showInformation("此单据流程不存在,请联系管理员", "提示");
                return;
            }


            // 如果流程已关闭
            if (flowDocInfo.getStatus() == DocStatus.CLOSED.getIndex()
                    || flowDocInfo.getStatus() == DocStatus.AUDITED.getIndex()
                    || flowDocInfo.getStatus() == DocStatus.WAITING_SETTLE.getIndex()
                    || flowDocInfo.getStatus() == DocStatus.SETTLING.getIndex()
                    || flowDocInfo.getStatus() == DocStatus.SETTLED.getIndex()) {
                comments = processService.findCommentByBusinessKey(businessKey);
            } else {  // 未关闭
                List<TaskInfo> tasks = processService.findTaskByBusinessKey(businessKey);
                if (tasks.size() <= 0) {
                    ZkUtils.showInformation("流程图出了问题,请联系管理员", "提示");
                    //LoggerUtil.getLogger().info("tasks.size():" + tasks.size());
                } else {
                    TaskInfo task = tasks.get(0);
                    Map<String, Object> tmpCoording = processService.findCoordingByTask(task.getId());

                    comments = processService.findCommentByTaskId(task.getId());
                    coording = tmpCoording;
                    System.out.println("x:" + coording.get("x"));
                    System.out.println("y:" + coording.get("y"));
                    System.out.println("width:" + coording.get("width"));
                    System.out.println("height:" + coording.get("height"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }


}
