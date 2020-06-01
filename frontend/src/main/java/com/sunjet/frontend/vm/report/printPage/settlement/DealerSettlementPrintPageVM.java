package com.sunjet.frontend.vm.report.printPage.settlement;

import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

//import net.sf.json.JSONArray;

/**
 * Created by lhj on 16/4/27.
 */
public class DealerSettlementPrintPageVM {


    @Getter
    @Setter
    private String reportUrl;

    @WireVariable
    RestClient restClient;

    @Init
    public void init() {
        String objId = Executions.getCurrent().getArg().get("objId").toString();
        String docNum = Executions.getCurrent().getArg().get("docNum").toString();
        String dealerPhone = Executions.getCurrent().getArg().get("dealerPhone").toString();
        String serviceManagerName = Executions.getCurrent().getArg().get("serviceManagerName").toString();
        ZkUtils.setSessionAttribute("restClient", restClient);

        this.reportUrl = "/views/report/printPage/settlement/dealerSettlementPrintPage.jsp?" +
                "objId=" + objId
                + "&docNum=" + docNum
                + "&dealerPhone=" + dealerPhone
                + "&serviceManagerName=" + serviceManagerName
                + "&printType=dealerSettlement.jasper";


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
    }
}
