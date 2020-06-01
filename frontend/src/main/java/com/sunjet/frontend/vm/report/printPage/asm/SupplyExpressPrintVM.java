package com.sunjet.frontend.vm.report.printPage.asm;

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
public class SupplyExpressPrintVM {


    @Getter
    @Setter
    private String reportUrl;

    @WireVariable
    RestClient restClient;

    @Init
    public void init() {
        String objId = Executions.getCurrent().getArg().get("objId").toString();
        String dealerAdderss = Executions.getCurrent().getArg().get("dealerAdderss").toString();
        String dealerName = Executions.getCurrent().getArg().get("dealerName").toString();
        String operatorPhone = Executions.getCurrent().getArg().get("operatorPhone").toString();
        String agencyName = Executions.getCurrent().getArg().get("agencyName").toString();
        String agencyAddress = Executions.getCurrent().getArg().get("agencyAddress").toString();
        String agencyPhone = Executions.getCurrent().getArg().get("agencyPhone").toString();
        String receive = Executions.getCurrent().getArg().get("receive").toString();
        String submitterName = Executions.getCurrent().getArg().get("submitterName").toString();
        ZkUtils.setSessionAttribute("restClient", restClient);

        this.reportUrl = "/views/report/printPage/asm/supplyExpressPrintPrint.jsp?objId=" + objId
                + "&dealerName=" + dealerName
                + "&agencyAddress=" + agencyAddress
                + "&dealerAdderss=" + dealerAdderss
                + "&receive=" + receive
                + "&operatorPhone=" + operatorPhone
                + "&agencyName=" + agencyName
                + "&agencyPhone=" + agencyPhone
                + "&submitterName=" + submitterName
                + "&printType=supplyExpressPrint.jasper";

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
    }
}
