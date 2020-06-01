package com.sunjet.frontend.vm.agency;

import com.sunjet.dto.asms.agency.AgencyQuitRequestInfo;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.agency.AgencyQuitService;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/9/8.
 */
public class AgencyQuitFormVM extends FormVM {

    @WireVariable
    private AgencyQuitService agencyQuitService;
    @WireVariable
    private AgencyService agencyService;

    @Getter
    @Setter
    private AgencyQuitRequestInfo agencyQuitRequest = new AgencyQuitRequestInfo();

    @Getter
    @Setter
    private AgencyInfo agency = new AgencyInfo();        //合作商对象

    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();


    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            this.agencyQuitRequest = agencyQuitService.findOne(this.objId);
            //AgencyInfo agencyInfo = agencyService.findOne(this.agencyQuitRequest.getAgency());
            //this.agencyQuitRequest.setAgencyInfo(agencyInfo);

        } else {
            this.agencyQuitRequest = new AgencyQuitRequestInfo();
            this.agencyQuitRequest.setAgencyInfo(new AgencyInfo());

        }
        this.setActiveUserMsg(this.agencyQuitRequest);
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgencies(@BindingParam("model") String keyword) {
        if (getActiveUser().getAgency() != null) {   // 合作库用户
            this.agencies.clear();
            this.agencies.add(getActiveUser().getAgency());
        } else if (getActiveUser().getDealer() != null) {  // 服务站用户
            this.agencies.clear();
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword.trim());
        }
    }

    @Command
    @NotifyChange("agencyQuitRequest")
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agency = agency;
        this.agencyQuitRequest.setAgencyInfo(agency);
        this.agencyQuitRequest.setAgency(agency.getObjId());
    }

    /**
     * 保存实体
     */
    @Command
    //@NotifyChange("*")
    public void submit() {
        if (this.agencyQuitRequest.getAgencyInfo().getCode() == null) {
            ZkUtils.showInformation("请选合作商再保存", "提示");
            return;
        }
        this.agencyQuitRequest.setAgencyInfo(getAgency());
        this.agencyQuitRequest = agencyQuitService.save(agencyQuitRequest);
        flowDocInfo = this.agencyQuitRequest;
        //BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_QIUT_LIST, null);
        this.updateUIState();
        showDialog();
    }

    /**
     * 检验
     *
     * @return
     */
    @Override
    protected Boolean checkValid() {
        if (this.agencyQuitRequest.getAgencyInfo().getCode() == null) {
            ZkUtils.showExclamation("合作商不能为空！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.agencyQuitRequest.getReason())) {
            ZkUtils.showExclamation("请填写退出原因！", "系统提示");
            return false;
        }
        return true;
    }

    @Command
    public void startProcess() {
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.agencyQuitRequest)) {
                    this.agencyQuitRequest = this.agencyQuitService.save(this.agencyQuitRequest);
                    flowDocInfo = this.agencyQuitRequest;
                    Map<String, String> map = this.agencyQuitService.startProcess(this.agencyQuitRequest, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.updateUIState();
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });
    }


    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                agencyQuitService.desertTask(this.agencyQuitRequest.getObjId());
                canHandleTask = false;
                canDesertTask = false;
                canEdit = false;
                this.updateUIState();
                showDialog();
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消作废", "提示");
            }
        });
    }

    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return agencyQuitService.save((AgencyQuitRequestInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return agencyQuitService.findOne(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("agencyQuitRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_QIUT_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

}
