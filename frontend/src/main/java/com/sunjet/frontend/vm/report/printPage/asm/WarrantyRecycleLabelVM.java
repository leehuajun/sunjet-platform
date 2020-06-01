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
public class WarrantyRecycleLabelVM {

    @Getter
    @Setter
    private String reportUrl;

    @WireVariable
    RestClient restClient;

    @Init
    public void init() {
        String objId = (String) Executions.getCurrent().getArg().get("objId");
        String printType = "recycleLabel.jasper";
        ZkUtils.setSessionAttribute("restClient", restClient);

        this.reportUrl = "/views/report/printPage/asm/warrantyRecycleLabel.jsp?objId=" + objId
                + "&printType=" + printType;


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
    }
}
