package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import static com.sunjet.frontend.utils.common.CommonHelper.genPassword;
import static com.sunjet.frontend.vm.system.UserFormVM.rexCheckPassword;

/**
 * @author zyf
 * @create 2017-7-13 上午11:08
 */
public class ChangePasswordFormVM extends FormVM {

    @WireVariable
    private UserService userService;

    @Getter
    @Setter
    private UserInfo user;

    @Getter
    @Setter
    private String originPassword;
    @Getter
    @Setter
    private String newPassword;
    @Getter
    @Setter
    private String newPasswordConfirm;

    @Getter
    @Setter
    private Window window;


    @Getter
    @Setter
    private String updatePassWordUser = "";   //修改密码的用户

    @Init(superclass = true)
    public void init() {
        String objId = (String) Executions.getCurrent().getArg().get("objId");
        if (StringUtils.isNotBlank(objId)) {
            user = userService.findOne(objId);
            updatePassWordUser = "admin";
        } else {
            user = userService.findOne(getActiveUser().getUserId());
        }
    }


    @Command
    public void changePassword(@BindingParam("event") Window win) {
        Integer hashInterations = 1;
        //if (this.originPassword == null) {
        //    ZkUtils.showError("请输入原密码", "系统提示");
        //    return;
        //}
        if (this.newPassword == null) {
            ZkUtils.showError("请输入新密码", "系统提示");
            return;
        } else if (this.newPasswordConfirm == null) {
            ZkUtils.showError("请确认密码", "系统提示");
            return;
        }
        if (this.newPassword.trim().equals(this.newPasswordConfirm.trim()) == false) {
            ZkUtils.showExclamation("两次输入的密码不一致，请重新输入!", "系统提示");
            this.newPassword = "";
            this.newPasswordConfirm = "";
            return;
        }
        //String originCredentials = EncryptTool.generatePasswordMd5(this.originPassword, user.getSalt(), hashInterations);

        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        //if (!originCredentials.equals(user.getPassword())) {
        //    ZkUtils.showExclamation("原密码不正确，请重新输入!", "系统提示");
        //    this.originPassword = "";
        //    return;
        //} else {
        if (!rexCheckPassword(this.newPasswordConfirm)) {

            ZkUtils.showError("密码不少于6位，必须包含字母大、小写、数字或特殊字符（_!@#%）", "提示");
            return;
        }
        //}

        user.setPassword(this.newPassword);
        UserInfo userEntity1 = genPassword(user);
        if (StringUtils.isNotBlank(updatePassWordUser)) {
            ZkUtils.showQuestion("是否确认修改", "系统提示", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        // 用户点击的是确定按钮
                        userService.changePassword(userEntity1);
                        win.detach();
                    } else {
                        // 用户点击的是取消按钮
                    }
                }
            });

        } else {
            ZkUtils.showQuestion("密码修改成功！\r请重新登录", "系统提示", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        // 用户点击的是确定按钮
                        userService.changePassword(userEntity1);
                        Executions.sendRedirect("/logout.zul");
                    } else {
                        // 用户点击的是取消按钮
                    }
                }
            });
        }


    }

}
