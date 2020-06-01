package com.sunjet.frontend.vm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.User;
import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.frontend.auth.AuthHelper;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.CustomTreeNode;
import com.sunjet.frontend.utils.zk.MenuTreeUtil;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.BaseVM;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: lhj
 * @create: 2017-07-01 12:09
 * @description: 说明
 */
@Slf4j
public class IndexVM extends BaseVM {

    private static final String URL = "http://localhost:9000/products";
    private static Logger logger = LoggerFactory.getLogger(IndexVM.class);
    @WireVariable
    RestClient restClient;
    @Wire
    A amodule, atask, anoti, amsg;
    private ObjectMapper mapper = new ObjectMapper();
    @Getter
    @Setter
    private String westTitle = "功能导航";

    @Getter
    private Integer count = 0;

    @Getter
    @Setter
    private TreeModel treeModel;

    @WireVariable
    private RestTemplate restTemplate;

    //    @WireVariable
//    private UserService userService;
    @Getter
    @Setter
    private List<TaskInfo> tasks = new ArrayList<>();    //流程任务列表


    @Setter
    @Getter
    private List<NoticeInfo> showNotices = new ArrayList<>();

    @Getter
    @Setter
    private String message = "Please Input Message...";

    @Getter
    @Setter
    private String iconStatus = "z-icon-chevron-down";

    @Getter
    @Setter
    private Map<String, Boolean> mapMenuStatus = new HashMap<>();
    @Getter
    @Setter
    private Map<String, String> mapMenuIcon = new HashMap<>();
    @Getter
    @Setter
    private String now;
    @Getter
    @Setter
    private Boolean sidebarStatus = true;
    @Getter
    @Setter
    private String westWidth = "260px";
    @Getter
    @Setter
    private Boolean enableNotice = false;   //启用公告


//    @Getter
//    private List<Product> products = new ArrayList<>();

    @Init
    public void init() throws IOException {
        this.userType = getActiveUser().getUserType();
        if (getActiveUser().getLogId().equals("admin")) {
            List<MenuInfo> menus = menuService.findAll();

            this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menus));

            menus.forEach(menuInfo -> {
                if (menuInfo.getParent() == null) {
                    mapMenuIcon.put(menuInfo.getObjId(), "z-icon-chevron-down");
                    mapMenuStatus.put(menuInfo.getObjId(), false);
                }
//                if(menuInfo.c)
            });

        } else {
            List<MenuInfo> menus = getActiveUser().getMenus();

            menus.forEach(menuInfo -> {
                if (menuInfo.getParent() == null) {
                    mapMenuIcon.put(menuInfo.getObjId(), "z-icon-chevron-down");
                    mapMenuStatus.put(menuInfo.getObjId(), false);
                }
            });

            this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menus));
        }

        /**
         * 获取公告
         */
        this.setNotices(this.getNoticeList());
        this.setEnableNotice(hasPermission("NoticeEntity:search"));

        /**
         * 获取消息通知
         */
        this.setMessages(this.getUnReadMessageList());

        /**
         * 获取任务列表
         */
        this.setTasks(this.getTaskList());

        for (int i = 0; i < this.getNotices().size() && i < CommonHelper.LEN_SHOW; i++) {
            showNotices.add(this.getNotices().get(i));
        }


    }

    /**
     * 点击tree右边的icon
     * 展开tree，icon切换样式
     *
     * @param e
     */
    @NotifyChange({"mapMenuStatus", "mapMenuIcon"})
    @Command
    public void toggleicon(@BindingParam("e") Event e) {
        System.out.println(e);
        Treeitem treeitem = (Treeitem) e.getTarget().getParent().getParent().getParent();
        CustomTreeNode node = (CustomTreeNode) treeitem.getValue();
        if (treeitem.isOpen()) {
            mapMenuIcon.put(((MenuInfo) node.getData()).getObjId(), "z-icon-chevron-down");
//                mapMenuStatus.put(((MenuInfo) node.getData()).getObjId(),false);
            mapMenuIcon.keySet().forEach(key -> {
                if (((MenuInfo) node.getData()).getObjId() != key) {
                    mapMenuIcon.put(key, "z-icon-chevron-down");
                }
            });
            treeitem.setOpen(false);

            mapMenuStatus.put(((MenuInfo) node.getData()).getObjId(), false);
        } else {
            mapMenuIcon.put(((MenuInfo) node.getData()).getObjId(), "z-icon-chevron-up");
            mapMenuIcon.keySet().forEach(key -> {
                if (((MenuInfo) node.getData()).getObjId() != key) {
                    mapMenuIcon.put(key, "z-icon-chevron-down");
                }
            });
            treeitem.setOpen(true);

            mapMenuStatus.keySet().forEach(key -> {
                if (((MenuInfo) node.getData()).getObjId() == key) {
                    mapMenuStatus.put(key, true);
                } else {
                    mapMenuStatus.put(key, false);
                }
            });
        }
    }

    /**
     * 打开标签
     *
     * @param e
     */
    @NotifyChange({"mapMenuStatus", "mapMenuIcon"})
    @Command
    public void openTab(@BindingParam("e") Event e) {
        System.out.println("权限验证结果:" + SecurityUtils.getSubject().isPermitted("items:add:1"));
        Treeitem treeitem = (Treeitem) e.getTarget();
//        System.out.println("树节点状态：" + treeitem.isOpen());
//        通过treeitem.getValue()方法也可以获得TreeNode对象
        CustomTreeNode node = (CustomTreeNode) treeitem.getValue();

//        if(node.getParent()==null){
//            mapMenuStatus.keySet().forEach( key ->{
//                if(((MenuInfo) node.getData()).getObjId()==key){
//                    mapMenuStatus.put(key,true);
//                }else{
//                    mapMenuStatus.put(key,false);
//                }
//
//            });
////            mapMenuStatus.put(((MenuInfo) node.getData()).getObjId(),false);
//        }
//
//        .z-tree .z-tree-body .z-treerow.z-treerow-selected .z-treecell .z-treecell-content

//        if (((MenuInfo) node.getData()).getParent() != null) {
////            treeitem.setStyle("border-right:4px solid red");
//            System.out.println("TreeRow:" + treeitem.getTreerow().toString());
//            treeitem.getTreerow().setStyle("border-right:4px solid red");
//            System.out.println("Treecell:" + treeitem.getTreerow().getChildren());
////            ((Treecell) treeitem.getTreerow().getChildren().get(0)).setStyle("border-right:4px solid red");
//        }
//        if (((MenuInfo) node.getData()).getParent() == null) {
//            if (treeitem.isOpen()) {
//                mapMenuStatus.put(((MenuInfo) node.getData()).getObjId(), false);
//            }else{
//                mapMenuStatus.keySet().forEach(key -> {
//                    if (((MenuInfo) node.getData()).getObjId() == key) {
//                        mapMenuStatus.put(key, true);
//                    } else {
//                        mapMenuStatus.put(key, false);
//                    }
//                });
//            }
//        }

        // 更改导航树的图标样式
        if (node.getChildCount() > 0) {
            if (treeitem.isOpen()) {
                mapMenuIcon.put(((MenuInfo) node.getData()).getObjId(), "z-icon-chevron-down");
//                mapMenuStatus.put(((MenuInfo) node.getData()).getObjId(),false);
                mapMenuIcon.keySet().forEach(key -> {
                    if (((MenuInfo) node.getData()).getObjId() != key) {
                        mapMenuIcon.put(key, "z-icon-chevron-down");
                    }
                });
                treeitem.setOpen(false);

                mapMenuStatus.put(((MenuInfo) node.getData()).getObjId(), false);
            } else {
                mapMenuIcon.put(((MenuInfo) node.getData()).getObjId(), "z-icon-chevron-up");
                mapMenuIcon.keySet().forEach(key -> {
                    if (((MenuInfo) node.getData()).getObjId() != key) {
                        mapMenuIcon.put(key, "z-icon-chevron-down");
                    }
                });
                treeitem.setOpen(true);

                mapMenuStatus.keySet().forEach(key -> {
                    if (((MenuInfo) node.getData()).getObjId() == key) {
                        mapMenuStatus.put(key, true);
                    } else {
                        mapMenuStatus.put(key, false);
                    }
                });
            }
        } else {
            MenuInfo menuInfo = (MenuInfo) node.getData();
            try {
                String url = (menuInfo.getUrl() == null || menuInfo.getUrl().trim().equalsIgnoreCase("null") || menuInfo.getUrl().trim().equals("")) ?
                        "/sorry.zul"
                        : menuInfo.getUrl();
                if (url.contains("http://")) {
                    ZkUtils.sendRedirect(url, "_blank");
                    return;
                }
                String iconSclass = menuInfo.getIcon();
                ZkTabboxUtil.newTab(menuInfo.getObjId(), menuInfo.getName() + "列表", iconSclass, true, ZkTabboxUtil.OverFlowType.AUTO, url, null);
            } catch (TabDuplicateException ex) {
                ex.printStackTrace();
            }
        }
    }
//    public String getStatus(String menuId){
//
//    }

//    @Command
//    public String getStatus(@BindingParam("e") Event e){
//        Treeitem treeitem = (Treeitem) e.getTarget();
//        if (treeitem.isOpen()) {
//            return "z-icon-angle-up";
//        } else {
//            return "z-icon-angle-down";
//        }
//    }


    @Command
    public void openNode(@BindingParam("e") Event e) {
        Treecell treecell = (Treecell) e.getTarget();
        Treeitem treeitem = (Treeitem) treecell.getParent().getParent();
//        通过treeitem.getValue()方法也可以获得TreeNode对象
        CustomTreeNode currentNode = (CustomTreeNode) treeitem.getValue();
        logger.info("子节点数量:" + currentNode.getChildCount());
        if (currentNode.getChildCount() > 0) {
            treeitem.setOpen(treeitem.isOpen() ? false : true);
        } else {


//        MenuEntity menuEntity = (MenuEntity)this.selectedNode.getData();
            MenuInfo menuEntity = (MenuInfo) currentNode.getData();
            logger.info("MenuEntity对象内容:" + menuEntity);
        }
    }


    @Listen("onOpen = #modulepp")
    public void toggleModulePopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), amodule);
    }

    @Listen("onOpen = #taskpp")
    public void toggleTaskPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), atask);
    }

    @Listen("onOpen = #notipp")
    public void toggleNotiPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), anoti);
    }

    @Listen("onOpen = #msgpp")
    public void toggleMsgPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), amsg);
    }


    // Toggle open class to the component

    public void toggleOpenClass(Boolean open, Component component) {
        HtmlBasedComponent comp = (HtmlBasedComponent) component;
        String scls = comp.getSclass();
        if (open) {
            comp.setSclass(scls + " open");
        } else {
            comp.setSclass(scls.replace(" open", ""));
        }
    }


    /**
     * 获取客户端信息
     *
     * @param event
     */
    @Command
    public void showClientInfo(@BindingParam("evt") ClientInfoEvent event) {
        CommonHelper.windowHeight = event.getDesktopHeight();
        CommonHelper.windowWidth = event.getDesktopWidth();
        CommonHelper.screenHeight = event.getScreenHeight();
        // baseGridHeight，用于列表视图，windowHeight为视窗内的高度
        // 高度为：windowHeight - navbar的高度 - tab的高度 - 分页的高度 - 各个padding的高度
        CommonHelper.baseGridHeight = CommonHelper.windowHeight - 234 - 81;
        logger.info(String.format("Desktop: width:%s x height:%s", event.getDesktopWidth(), event.getDesktopHeight()));
        logger.info(String.format("Screen: width:%s x height:%s", event.getScreenWidth(), event.getScreenHeight()));
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
//        view.getPage().setStyle("background-color:rgb(78,116,149)");
        showIndex();
    }

    @Command
    public void sayHello() {
        Messagebox.show(message);
        log.info(message);
    }

    /**
     * 返回对象集合的json字符串
     *
     * @throws JsonProcessingException
     */
    @Command
    public void getProductsJson() throws JsonProcessingException {
        ResponseEntity<String> entity = restTemplate.exchange(URL, HttpMethod.GET, AuthHelper.getHttpEntity(), String.class);

        log.info(entity.getBody());
        Messagebox.show(entity.getBody());
    }

    /**
     * 返回对象集合
     *
     * @throws JsonProcessingException
     */
    @Command
    public void getProducts() throws JsonProcessingException {
        ResponseEntity<List> entity = restTemplate.exchange(URL, HttpMethod.GET, AuthHelper.getHttpEntity(), List.class);
//        List<Product> list = entity.getBody();
        log.info(entity.getBody().toString());
        Messagebox.show(entity.getBody().toString());

    }


    /**
     * 返回对象
     */
    @Command
    public void getUser() {

        try {
            ResponseEntity<User> entity = restTemplate.exchange("http://localhost:9000/user/admin", HttpMethod.GET, AuthHelper.getHttpEntity(), User.class);
            log.info(entity.getBody().toString());
            Messagebox.show(entity.getBody().toString());
        } catch (RestClientException e) {
            e.printStackTrace();
            Messagebox.show(e.getMessage());
        }
    }

    /**
     * 返回对象的json字符串
     */
    @Command
    public void getUserJson() {
        ResponseEntity<String> entity = restTemplate.exchange("http://localhost:9000/user/admin", HttpMethod.GET, AuthHelper.getHttpEntity(), String.class);
        log.info(entity.getBody());
        Messagebox.show(entity.getBody());
    }

    /**
     * 显示服务站信息
     */
    @Command
    public void showDealerInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("objId", getActiveUser().getDealer().getObjId());
        String url = "/views/basic/dealer_form_win.zul";
        showWindow(url, paramMap);
    }

    /**
     * 显示合作商信息
     */
    @Command
    public void showAgencyInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("objId", getActiveUser().getAgency().getObjId());
        String url = "/views/basic/agency_form_win.zul";
        showWindow(url, paramMap);
    }


    /**
     * 创建窗口
     *
     * @param url
     * @param map
     */
    private void showWindow(String url, Map map) {
        Window wd = (Window) ZkUtils.createComponents(url, null, map);
        wd.setStyle("background:rgb(78,116,149)");
        wd.setStyle("padding:0px;");
        wd.doModal();
    }

    /**
     * 显示首页
     */
    @Command
    public void showIndex() {
        try {
//            ZkTabboxUtil.newTab("welcome", "欢迎", "z-icon-home", false, ZkTabboxUtil.OverFlowType.AUTO, "/welcome.zul", null);
            ZkTabboxUtil.newTab("portal", "首页", "z-icon-home", false, ZkTabboxUtil.OverFlowType.AUTO, "/portal.zul", null);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录
     */
    @Command
    public void logout() {
        Executions.sendRedirect("/logout.zul");
    }

    /**
     * 修改密码
     */
    @Command
    public void changePassword() {
        Window window = (Window) ZkUtils.createComponents("/views/system/change_password_form.zul", null, null);
        window.doModal();
    }

    /**
     * 公告
     *
     * @throws TabDuplicateException
     */
    @Command
    public void enterNoticeList() throws TabDuplicateException {
        enterList("/views/basic/notice_list.zul");
    }

    /**
     * 流程任务
     *
     * @throws TabDuplicateException
     */
    @Command
    public void enterTaskList() throws TabDuplicateException {
        enterList("/views/flow/task_list.zul");
    }

    @Command
    public void enterMessageList() throws TabDuplicateException {
        enterList("/views/system/message_list.zul");
    }

    private void enterList(String url) throws TabDuplicateException {
        MenuInfo menu = menuService.findMenuByUrl(url);
        ZkTabboxUtil.newTab(menu.getObjId(), menu.getName(), null, true,
                ZkTabboxUtil.OverFlowType.SCROLL, url, null);
    }

    @Command
    public void closeOne(@BindingParam("tabNow") Tab tabNow) {
        ZkTabboxUtil.closeOne(tabNow);
    }

    @Command
    public void closeAll(@BindingParam("tabs") List<Tab> tabList) {
        ZkTabboxUtil.closeAll(tabList);
    }

    @NotifyChange("now")
    @Command
    public void renewal() {
//        LocalDateTime localDateTime = LocalDateTime.now().get
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = sdf.format(new Date());
        log.info("用户Session续约, 当前时间: " + sdf.format(new Date()));
    }

    @NotifyChange("sidebarStatus")
    @Command
    public void toggleSidebar() {
        this.sidebarStatus = !this.sidebarStatus;
//        westWidth = westWidth.equals("0px") ? "260px" : "0px";
//        log.info(westWidth);
    }
}
