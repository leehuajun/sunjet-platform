package com.sunjet.frontend.vm;

import com.sunjet.frontend.service.system.ConfigService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-07-02 21:50
 * @description: 说明
 */
@Slf4j
public class LoginVM {

    @WireVariable
    public ConfigService configService;

    @Getter
    @Setter
    private String technicalSupportandversion;

    @Init
    @NotifyChange("*")
    public void init() {
        log.info("启动登录界面！" + new Date());
        String technicalSupport = configService.getValueByKey("technicalSupport");
        String version = configService.getValueByKey("app_version");
        technicalSupportandversion = technicalSupport + "         " + version;
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            Executions.sendRedirect("/logout.zul");
//      subject.logout();
//      Executions.sendRedirect("/index.zul");
        }
    }
}
