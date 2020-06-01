package com.sunjet.frontend.vm.base;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.AgencyItem;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lhj on 16/11/21.
 */
public class AgencyListBaseVM<T> extends ListVM<T> {

    @Setter
    @Getter
    public AgencyItem agencyItem = new AgencyItem();

    @Setter
    @Getter
    public AgencyInfo agency = new AgencyInfo();

    @Setter
    @Getter
    private List<AgencyInfo> agencies = new ArrayList<>();

    @Getter
    @Setter
    private String userType = "";

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    public DocStatus status = DocStatus.ALL;

    @Init(superclass = true)
    public void agencyListInit() {
        //this.setHeaderRows(1);   // 设置搜索栏的行数，默认是1行

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
    @NotifyChange({"agency", "keyword"})
    public void clearSelectedAgency() {
        this.agency = getActiveUser().getAgency();
        this.setKeyword("");
    }

}
