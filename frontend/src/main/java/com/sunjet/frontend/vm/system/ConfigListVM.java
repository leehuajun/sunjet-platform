package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.frontend.service.system.ConfigService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * @author zyf
 * @create 2017-7-13 上午10:59
 */
public class ConfigListVM extends ListVM<ConfigInfo> {

    @WireVariable
    private ConfigService configService;

    @Getter
    @Setter
    private ConfigInfo configInfo = new ConfigInfo();

    @Init
    public void init() {
        this.setEnableAdd(hasPermission("ConfigEntity:create"));
        this.setEnableUpdate(hasPermission("ConfigEntity:modify"));
        this.setEnableDelete(hasPermission("ConfigEntity:delete"));
        this.setTitle("参数配置");
        this.setFormUrl("/views/system/config_form.zul");
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = configService.getPageList(pageParam);
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        //pageParam.setPage(pageResult.getPage());
        //pageParam.setPageSize(pageResult.getPageSize());
        //pageParam.setInfoWhere(configInfo);
        refreshPage(configInfo);
        getPageList();
    }


    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //pageParam = new PageParam<>();
        //pageParam.setInfoWhere(configInfo);

        refreshFirstPage(configInfo);

        getPageList();
    }

    /**
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId) {
        try {

            //JSONObject jsonObject = JSONObject.fromObject(map);
            //
            //ConfigInfo info = JsonHelper.json2Bean(jsonObject.toString(), ConfigInfo.class);
            //ConfigInfo info = JsonHelper.map2Bean(objId, ConfigInfo.class);

            ZkUtils.showQuestion(
                    "\r是否确定删除?", "询问", new EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (event.getName().equals("onOK")) {
                                configService.delete(objId);
                                BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_CONFIG_LIST, null);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_CONFIG_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        gotoPageNo(null);
        this.closeDialog();
    }


//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
//        Selectors.wireComponents(view, this, false);
//
//    }
//
//    /***
//     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
//     */
//    protected void configSearchOrder(SearchDTO searchDTO) {
//        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
//        searchDTO.setSearchOrderList(Arrays.asList(
//                new SearchOrder("configKey", SearchOrder.OrderType.ASC, 1)
//        ));
//    }
//
//    /***
//     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
//     */
//    protected void configSpecification(SearchDTO searchDTO) {
//        if (!this.getKeyword().equals("")) {
//            Specification<ConfigEntity> specification = new Specification<ConfigEntity>() {
//                @Override
//                public Predicate toPredicate(Root<ConfigEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                    Predicate p01 = CustomRestrictions.like("configKey", getKeyword(), true).toPredicate(root, query, cb);
////                    Predicate p02 = CustomRestrictions.like("logId",getKeyword(),true).toPredicate(root,query,cb);
////                    Predicate p = cb.or(p01,p02);
//                    return p01;
//                }
//            };
//            searchDTO.setSpecification(specification);
//        }
//    }
//
//    @GlobalCommand(GlobalCommandValues.REFRESH_CONFIG_LIST)
//    @NotifyChange("resultDTO")
//    public void refreshUserList() {
//        this.refreshList();
//    }
//
//    @GlobalCommand(GlobalCommandValues.REFRESH_CONFIG_ENTITY)
//    @NotifyChange("*")
//    public void refreshUserEntity(@BindingParam("entity") DocEntity entity) {
//        this.refreshEntity(entity);
//    }
//

}
