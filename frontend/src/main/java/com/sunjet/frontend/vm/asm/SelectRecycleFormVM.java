package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.recycle.RecycleNoticePendingInfo;
import com.sunjet.frontend.service.recycle.RecycleNoticeItemService;
import com.sunjet.frontend.service.recycle.RecycleNoticeService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class SelectRecycleFormVM extends FormVM {
    protected final static Logger logger = LoggerFactory.getLogger(SelectRecycleFormVM.class);

    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;
    @WireVariable
    private RecycleNoticeService recycleNoticeService;

    @Getter
    @Setter
    private String data = "";
    @Getter
    @Setter
    private String dealerCode = "";
    @Getter
    @Setter
    private List selectedRecycleList = new ArrayList<>();
    @Getter
    @Setter
    private List<RecycleNoticePendingInfo> partList = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        logger.info(Executions.getCurrent().getArg().get("id").toString());
        dealerCode = Executions.getCurrent().getArg().get("dealerCode").toString();
    }

    @Command
    @NotifyChange("*")
    public void searchRecycle() {
        try {
            this.partList.clear();
            List<RecycleNoticePendingInfo> recycleNoticeItems = recycleNoticeItemService.findByReturnOrParts(this.data.trim(), dealerCode.trim());

            if (recycleNoticeItems.size() < 1) {
                ZkUtils.showInformation("未查询到相关物料！", "提示");
                return;
            }
            this.partList.addAll(recycleNoticeItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

}
