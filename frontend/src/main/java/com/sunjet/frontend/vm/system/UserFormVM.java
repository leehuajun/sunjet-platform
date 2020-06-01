package com.sunjet.frontend.vm.system;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.RoleInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.system.RoleService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.model.EntityWrapper;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.utils.common.JsonHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.sunjet.frontend.utils.common.CommonHelper.genPassword;


/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
@Slf4j
public class UserFormVM extends FormVM {

    @WireVariable
    private UserService userService;
    @WireVariable
    private RoleService roleService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private DealerService dealerService;

    @Getter
    @Setter
    private String keywordAgency = "";
    @Getter
    @Setter
    private String keywordDealer = "";
    @Getter
    @Setter
    private String passwordConfirm;
    @Getter
    @Setter
    private List<AgencyInfo> agencies = new ArrayList<>();
    @Getter
    @Setter
    private List<DealerInfo> dealers = new ArrayList<>();
    @Getter
    @Setter
    private List<EntityWrapper<RoleInfo>> entityWrappers = new ArrayList<>();
    @Getter
    @Setter
    private List<EntityWrapper<RoleInfo>> entityWrapperSelectedItems = new ArrayList<>();
    @Getter
    @Setter
    private UserInfo userInfo;
    @Getter
    @Setter
    private Boolean wulinType = false;

    @Getter
    @Setter
    private Boolean dealerType = false;

    @Getter
    @Setter
    private Boolean agencyType = false;


    @Init(superclass = true)
    public void init() {
        try {
            if ("admin".equals(getActiveUser().getLogId())) {
                wulinType = true;
                dealerType = true;
                agencyType = true;
            } else {
                if (getActiveUser().getRoles() != null) {
                    for (RoleInfo roleInfo : getActiveUser().getRoles()) {
                        if ("role50".equals(roleInfo.getRoleId())) {
                            dealerType = true;
                            agencyType = true;
                        } else {
                            wulinType = true;
                        }
                        ;
                    }
                }

            }

            entityWrappers.clear();
            //所有角色
            List<RoleInfo> roles = roleService.findAll();

            if (StringUtils.isNotBlank(objId)) {

                //获取用户及用户关联的角色
                userInfo = userService.findOne(objId);

                this.passwordConfirm = userInfo.getPassword();

                for (Object o : roles) {

                    RoleInfo roleInfo = JsonHelper.map2Bean(o, RoleInfo.class);

                    //判断是否有关系角色
                    Boolean found = false;
                    if (userInfo.getRoles() != null) {
                        for (RoleInfo model : userInfo.getRoles()) {
                            if (model.getObjId().equals(roleInfo.getObjId())) {
                                found = true;
                                break;
                            }
                        }
                    }
                    //勾选已关联的角色
                    if (found) {
                        EntityWrapper<RoleInfo> modelWrapper = new EntityWrapper<>(roleInfo, true);
                        entityWrappers.add(modelWrapper);
                        entityWrapperSelectedItems.add(modelWrapper);
                    } else {
                        //不勾选
                        entityWrappers.add(new EntityWrapper(roleInfo, false));
                    }

                }
                this.readonly = true;
            } else {
                //新增
                userInfo = new UserInfo();
                for (Object o : roles) {
                    RoleInfo roleInfo = JsonHelper.map2Bean(o, RoleInfo.class);
                    entityWrappers.add(new EntityWrapper(roleInfo, false));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    public void submit() {
        if (userInfo != null) {
            //新增
            if (StringUtils.isBlank(userInfo.getObjId())) {
                if (StringUtils.isNotBlank(userInfo.getName())) {
                    if (userInfo.getName().length() < 2) {
                        ZkUtils.showInformation("请填写姓名并且不少于两个字", "提示");
                        return;
                    }
                } else {
                    ZkUtils.showInformation("请填写姓名并且不少于两个字", "提示");
                    return;
                }

                if (StringUtils.isNotBlank(userInfo.getLogId())) {
                    if (userInfo.getLogId().length() < 4) {
                        ZkUtils.showInformation("请填写登陆名并且不少于四个字", "提示");
                        return;
                    } else {
                        UserInfo user = userService.findOneByLogId(userInfo.getLogId());
                        if (user != null && StringUtils.isNotBlank(user.getLogId())) {
                            ZkUtils.showInformation("登陆名已存在", "提示");
                            return;
                        }
                    }
                } else {
                    ZkUtils.showInformation("请填写登陆名并且不少于四个字", "提示");
                    return;
                }
                if (userInfo.getPassword() == null) {
                    ZkUtils.showError("密码不能为空", "提示");
                    return;
                } else if (!userInfo.getPassword().equals(this.passwordConfirm)) {
                    ZkUtils.showError("两次输入的密码不一致", "提示");
                    return;
                } else if (userInfo.getPassword().equals(this.passwordConfirm)) {
                    if (!rexCheckPassword(userInfo.getPassword())) {

                        ZkUtils.showError("密码不少于6位，必须包含字母大、小写、数字或特殊字符（_!@#%）", "提示");
                        return;
                    }

                }
                if (StringUtils.isNotBlank(userInfo.getEmail())) {
                    if (!rexCheckEmail(userInfo.getEmail())) {
                        ZkUtils.showError("请输入正确的邮箱格式", "提示");
                        return;
                    }
                }


            } else {
                if (StringUtils.isNotBlank(userInfo.getName())) {
                    if (userInfo.getName().length() < 2) {
                        ZkUtils.showInformation("请填写姓名并且不少于两个字", "提示");
                        return;
                    }
                } else {
                    ZkUtils.showInformation("请填写姓名并且不少于两个字", "提示");
                    return;
                }
            }

            if (StringUtils.isBlank(userInfo.getUserType())) {
                ZkUtils.showInformation("请选择用户类型", "提示");
                return;
            }

            if (StringUtils.isNotBlank(userInfo.getEmail())) {
                if (!rexCheckEmail(userInfo.getEmail())) {
                    ZkUtils.showError("请输入正确的邮箱格式", "提示");
                    return;
                }
            }
        } else {
            ZkUtils.showError("系统错误,请联系管理员", "警告");
            return;
        }


        if (userInfo.getRoles() != null) {
            this.userInfo.getRoles().clear();
        } else {
            this.userInfo.setRoles(new ArrayList<>());
        }

        for (EntityWrapper<RoleInfo> entityWrapper : entityWrapperSelectedItems) {
            this.userInfo.getRoles().add(entityWrapper.getEntity());
        }
        if (userInfo.getRoles().size() < 1) {
            ZkUtils.showInformation("请至少选择一个角色！", "提示");
            return;
        }
//        if (userInfo.getRoles().size() > 0) {
//            userInfo.setRolesDesc(userInfo.getRoles().toString().replace("[", "").replace("]", ""));
//        }
//        UserInfo user = genPassword(userInfo);
        UserInfo user = userInfo;
        if (userService.findOne(user.getObjId()) == null) {
            user = genPassword(userInfo);
        }


        UserInfo save = userService.save(user);
        if (save != null) {
            showDialog();
        } else {
            ZkUtils.showError("保存失败", "提示");
        }

        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_USER_LIST, null);
    }

    @Command
    public void changeCheckboxStatus(@BindingParam("ctrl") Component comp) {
        Checkbox checkbox = (Checkbox) comp;
        checkbox.setChecked(checkbox.isChecked() ? false : true);
    }

    @Command
    @NotifyChange("userInfo")
    public void checkUserType() {
        if (this.userInfo == null) {
            log.info("userInfo is null!");
            return;
        }
        switch (userInfo.getUserType()) {
            case "agency":
                this.userInfo.setDealer(null);
                break;
            case "dealer":
                this.userInfo.setAgency(null);
                break;
            case "wuling":
                this.userInfo.setAgency(null);
                this.userInfo.setDealer(null);
                break;
        }
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgence() {
        agencies = agencyService.findAllByKeyword(this.keywordAgency);
    }

    @Command
    @NotifyChange("dealers")
    public void searchDealer() {
        dealers = dealerService.findAllByKeyword(this.keywordDealer);
    }

    @Command
    @NotifyChange("userInfo")
    public void selectAgency(@BindingParam("model") AgencyInfo agency) {
        try {
            this.userInfo.setAgency(agency);
            this.userInfo.setDealer(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("*")
    public void selectDealer(@BindingParam("model") LinkedHashMap map) {
        try {
            DealerInfo dealer = JsonHelper.map2Bean(map, DealerInfo.class);
            this.userInfo.setDealer(dealer);
            this.userInfo.setAgency(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean rexCheckPassword(String password) {
        // 6-20 位，字母、数字、字符
        //String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        String regStr = "^((?=.*[0-9].*)(?=.*[A-Za-z].*))[_0-9A-Za-z@!#%]{6,16}$";
        return password.matches(regStr);
    }

    public static boolean rexCheckEmail(String email) {
        String regStr = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regStr);
    }


    /**
     * zk 页面格式化数据
     *
     * @param roleInfo
     * @return
     */
    public String formatRoles(RoleInfo roleInfo) {
        String format = "";
        try {
            if (roleInfo != null) {
                format = roleInfo.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format;
    }


}
