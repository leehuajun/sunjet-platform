package com.sunjet.backend;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
public class recycleLabel {

    @Id
    private String objId;      // 主键id
    private String partCode;  //配件号
    private String partName;  //配件名
    private String srcDocNo;  // 来源单号
    private String srcDocType;  // 来源单号类型
    private String dealerCode;  //服务站编码
    private String dealerName;  // 服务站名
    private Date requestDate;  // 返回时间
    private String vin;         // 车辆vin
    private String vehicleModel;  //车辆型号
    private String engineNo;       // 发动机号
    private String purchaseDate;   //购买时间
    private String mileage;     // 行车里程
    private String pattern;
    private String reason;


}
