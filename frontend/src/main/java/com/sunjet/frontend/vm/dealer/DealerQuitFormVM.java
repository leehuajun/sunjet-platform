package com.sunjet.frontend.vm.dealer;

import com.sunjet.dto.asms.basic.*;
import com.sunjet.dto.asms.dealer.DealerQuitRequestInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.dealer.DealerQuitService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.*;

/**
 * 服务站退出申请
 *
 * @author zyf
 * Created on 2016/9/8.
 */
public class DealerQuitFormVM extends FormVM {

    @WireVariable
    private DealerQuitService dealerQuitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private DictionaryService dictionaryService;

    @Getter
    @Setter
    private DealerQuitRequestInfo dealerQuitRequest = new DealerQuitRequestInfo();

    @Getter
    @Setter
    private List<UserInfo> serviceManagers = new ArrayList<>();
    @Getter
    @Setter
    private List<String> strOtherCollaborations = new ArrayList<>();
    @Getter
    @Setter
    private List<String> strProductsOfMaintains = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> selectedOtherCollaborations = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> selectedProductsOfMaintains = new ArrayList<>();
    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities;
    @Getter
    @Setter
    private List<CityInfo> cityEntities = new ArrayList<>();
    @Getter
    @Setter
    private List<CountyInfo> countyEntities;
    @Getter
    @Setter
    private List<DealerInfo> parentDealers = new ArrayList<>();       // 列出所有父级服务站
    @Getter
    @Setter
    private Boolean canSelectParent = false;        // 是否可以选择父级服务站
    @Getter
    @Setter
    private List<DictionaryInfo> stars = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> qualifications = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> productsOfMaintains = new ArrayList<>();//拟维修我公司产品系列
    @Getter
    @Setter
    private List<DictionaryInfo> otherCollaborations = new ArrayList<>();//其他合作内容
    @Getter
    @Setter
    private Map<String, Object> variables = new HashMap<>();  //流程变量

    @Getter
    @Setter
    private DealerInfo dealerInfo = new DealerInfo();
    @Getter
    @Setter
    private RegionInfo city;

    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            dealerQuitRequest = dealerQuitService.findOneById(objId);
            dealerInfo = dealerService.findOneById(dealerQuitRequest.getDealerInfo().getObjId());
            city = regionService.findCityById(dealerInfo.getProvinceId());
        } else {
            dealerQuitRequest = new DealerQuitRequestInfo();
            dealerQuitRequest.setDealerInfo(new DealerInfo());
        }
        this.setProvinceEntities(regionService.findAllProvince());
        this.setStars(dictionaryService.findDictionariesByParentCode("10010"));
        this.setQualifications(dictionaryService.findDictionariesByParentCode("10020"));
        this.setProductsOfMaintains(dictionaryService.findDictionariesByParentCode("10050"));
        this.setOtherCollaborations(dictionaryService.findDictionariesByParentCode("10060"));
        this.setActiveUserMsg(this.dealerQuitRequest);


    }

    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_DEALER + filename;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
//    @NotifyChange("*")
    public void submit() {
        try {
            if (dealerInfo.getName() == null) {
                ZkUtils.showExclamation("请先选择服务站！", "系统提示");
                return;
            }
            dealerQuitRequest = dealerQuitService.save(dealerQuitRequest);
//            flowDocInfo = dealerQuitRequest;
//            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_QUIT_LIST, null);
            this.updateUIState();
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择服务站编号
     */
    @Command
    @NotifyChange({"dealerQuitRequest", "dealerInfo"})
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        dealerInfo = model;
        dealerInfo.setCode(model.getCode());
        dealerInfo.setName(model.getName());
        dealerQuitRequest.setDealerInfo(dealerInfo);
        dealerQuitRequest.setDealer(dealerInfo.getObjId());

    }

    @Override
    protected Boolean checkValid() {
        if (StringUtils.isBlank(this.dealerQuitRequest.getReason())) {
            ZkUtils.showError("请输入退出原因！", "系统提示");
            return false;
        }
        return true;
    }

    /**
     * 启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.dealerQuitRequest)) {
                    this.dealerQuitRequest = this.dealerQuitService.save(this.dealerQuitRequest);
                    flowDocInfo = this.dealerQuitRequest;
                    Map<String, String> map = this.dealerQuitService.startProcess(this.dealerQuitRequest, getActiveUser());
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
                ZkUtils.showInformation("取消作废", "提示");
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
                dealerQuitService.desertTask(this.dealerQuitRequest.getObjId());
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
        return dealerQuitService.save((DealerQuitRequestInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return dealerQuitService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("dealerQuitRequest", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null,
                GlobalCommandValues.REFRESH_DEALER_QUIT_LIST, null);
    }

}