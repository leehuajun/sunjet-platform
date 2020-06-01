package com.sunjet.backend.utils.common;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/10/20.
 * 报表导出工具
 */
public class ReportUtils {


    public static byte[] reportToPDF(Map<String, Object> parameters) {
        try {
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "report/";
            JasperReport report = (JasperReport) JRLoader.loadObject(new File(path + parameters.get("printType")));
            String dataSourceUrl = (String) parameters.get("dataSourceUrl");
            String userName = (String) parameters.get("userName");
            String password = (String) parameters.get("password");
            Connection conn = DriverManager.getConnection(dataSourceUrl, userName, password);
            System.out.println("Connection1 Successful!");
            byte[] bytes = JasperRunManager.runReportToPdf(report, parameters, conn);
            conn.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
