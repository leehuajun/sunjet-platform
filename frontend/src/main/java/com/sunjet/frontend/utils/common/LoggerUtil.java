package com.sunjet.frontend.utils.common;

//import org.apache.log4j.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lhj
 * @create 2015-12-23 下午11:33
 */
public class LoggerUtil {

    /**
     * 得到当前class名称，系统所有使用logger的地方直接调用此方法
     *
     * @return
     */
    public static Logger getLogger() {
        StackTraceElement[] stackEle = new RuntimeException().getStackTrace();
//        return Logger.getLogger(stackEle[1].getClassName());
        return LoggerFactory.getLogger(stackEle[1].getClassName());
    }
}
