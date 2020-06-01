package com.sunjet.frontend.vm.base;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.DealerItem;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.frontend.service.basic.RegionService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lhj on 16/11/21.
 */
public class DealerListBaseVM<T> extends ListVM<T> {

    @WireVariable
    private RegionService regionService;

    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities;
    @Getter
    @Setter
    public ProvinceInfo selectedProvince;// 选中的 省份/直辖市
    @Getter
    @Setter
    private Date startDate = new Date();    // 开始日期，绑定页面搜索的开始日期
    @Getter
    @Setter
    private Date endDate = new Date();      // 结束日期，绑定页面搜索的结束日期

    @Getter
    @Setter
    private List<DictionaryInfo> stars = new ArrayList<>();
    @Getter
    @Setter
    private List<DictionaryInfo> qualifications = new ArrayList<>();
    @Getter
    @Setter
    public DealerInfo dealer;
    @Getter
    @Setter
    public DealerItem dealerItem = new DealerItem();
    @Getter
    @Setter
    private List<DocStatus> documentStatuses = DocStatus.getListWithAll();
    @Getter
    @Setter
    private List<DictionaryInfo> productsOfMaintains = new ArrayList<>();//拟维修我公司产品系列
    @Getter
    @Setter
    private List<DictionaryInfo> otherCollaborations = new ArrayList<>();//其他合作内容


    @Getter
    @Setter
    public List<DictionaryInfo> listStar = new ArrayList<>();      // 星级
    @Getter
    @Setter
    public DictionaryInfo dealerStar = new DictionaryInfo();
    @Getter
    @Setter
    public DictionaryInfo qualification = new DictionaryInfo();
    @Getter
    @Setter
    public DocStatus status = DocStatus.ALL;

    @Init(superclass = true)
    public void dealerListInit() {
        this.setProvinceEntities(regionService.findAllProvince());
    }

}
