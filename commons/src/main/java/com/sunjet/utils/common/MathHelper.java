package com.sunjet.utils.common;

import java.math.BigDecimal;

/**
 * Created by lhj on 16/11/15.
 */
public class MathHelper {

    // 四舍五入 数字保留两位小数
    public static Double getDoubleAndTwoDecimalPlaces(Double value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
