package com.sunjet.backend.system.entity;

import com.sunjet.backend.base.DocEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 2015/9/6.
 * 用户实体
 */
@Data
//@Builder
//@NoArgsConstructor
@Entity
@Table(name = "SysUsers")
public class UserEntity extends DocEntity {
    private static final long serialVersionUID = -3786601921455785818L;
    /**
     * 登录名
     */
    @Column(name = "LogId", length = 100, nullable = false, unique = true)
    private String logId;
    /**
     * 加密后的密码
     */
    @Column(name = "Password", length = 50, nullable = false)
    private String password;
    /**
     * 密码加密 盐
     */
    @Column(name = "Salt", length = 40, nullable = false)
    private String salt;
    /**
     * 姓名
     */
    @Column(name = "Name", length = 50, nullable = false)
    private String name;
    /**
     * 电话
     */
    @Column(name = "Phone", length = 100)
    private String phone;

    /**
     * 电子邮件地址
     */
    @Column(name = "Email", length = 100)
    private String email;

    /**
     * e-mail 发送时间
     */
    private Date emailSendDate = new Date();

    /**
     * 用户类型   wuling:五菱工业用户  agency: 合作商  dealer: 服务站
     */
    @Column(length = 35)
    private String userType;

    @Column(length = 32)
    private String dealerId;//服务站id

    @Column(length = 32)
    private String agencyId;//合作商id



}
