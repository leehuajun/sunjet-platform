package com.sunjet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-07-03 20:19
 * @description: 说明
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;
    private Date createdTime;
    private String createrName;
    private Date modifiedTime;
    private String modifierName;
    private String logId;
    private String password;
    private String salt;
    private String name;
    private String phone;


}
