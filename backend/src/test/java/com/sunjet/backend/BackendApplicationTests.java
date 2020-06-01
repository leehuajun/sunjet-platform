package com.sunjet.backend;

import com.sunjet.backend.modules.asms.entity.report.DealerExpenseSummaryView;
import com.sunjet.backend.mq.Sender;
import com.sunjet.backend.system.repository.ScheduleJobRepository;
import com.sunjet.backend.system.repository.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.hibernate.annotations.Subselect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    @Value("${spring.datasource.url}")
    private String jdbcDataSource;//数据库链接
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private Sender sender;

    @Test
    public void contextLoads() {
    }

    @Test
    public void addUser() {
        //UserEntity entity = UserEntity.UserEntityBuilder.anUserEntity()
        //		.withName("admin")
        //		.withLogId("admin")
        //		.withPassword("5dfdd6a7ec1924ca2ac2898a570587fc")
        //		.withSalt("dacfw")
        //		.build();
        //UserEntity userEntity = userRepository.save(entity);
        //log.info(userEntity.toString());
    }


    @Test
    public void ReadTest() throws NotFoundException {
        Annotation subselect = DealerExpenseSummaryView.class.getAnnotation(Subselect.class);


        System.out.println(subselect);


    }


    @Test
    public void testTask() {
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee("liushigui").finished().list();


    }


    /**
     * 导出Pdf
     *
     * @throws ClassNotFoundException
     * @throws JRException
     * @throws SQLException
     * @throws FileNotFoundException
     */
    @Test
    public void outputPdf() throws ClassNotFoundException, SQLException, IOException, JRException {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("part_code", "Stslkdjasdkflas");


        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("F://recycleLabel.jasper"));

        Connection conn = DriverManager.getConnection(jdbcDataSource, userName, password);
        System.out.println("Connection1 Successful!");
        //byte[] bytes = JasperRunManager.runReportToPdf(report, parameters, conn);
        //conn.close();

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, new JREmptyDataSource());

        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        //FileOutputStream fileOutputStream = new FileOutputStream("D://1.xls");
        byte[] bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, new JREmptyDataSource());

        //JRDocxExporter exporter=new JRDocxExporter();
        //ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
        //exporter.setExporterInput(exporterInput);

        //设置输出项
        FileOutputStream fileOutputStream = new FileOutputStream("F://1.pdf");
        //OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput("F://1.pdf");
        //exporter.setExporterOutput(exporterOutput);
        //
        //
        //exporter.exportReport();
        //byte[] bytes = JasperRunManager.runReportToPdf(path+"activityMaintenance.jasper", parameters, conn);


        fileOutputStream.write(bytes);
        fileOutputStream.close();
        conn.close();
    }


    @Test
    public void reportDataforJavaBean() {
        //1.设定模板二进制文件路径，一定要可以通过该路径找到该文件
        String reportPath = "F:/testCode/recycle/recycleLabel.jasper";

        //2.设定报表的外部参数，map集合，这里要注意map的key值一定要与模板里Parameters的名字一致
        Map<String, Object> map = new HashMap<>();
        //3.新建一个集合，用于存放实体类对象
        ArrayList<recycleLabel> dataList = null;

        //4.用实体类对象集合生成一个可以传入的数据源
        JRBeanCollectionDataSource beanDataSource =
                new JRBeanCollectionDataSource(dataList);
        try {
            //3.通过JasperFillManager工具进行填充报表，填充成功后会生成一个JasperPring文件，该文件用于输出
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanDataSource);
            //4.设定目标文件输出的路径
            String desFilePath = "f:/JasperSampleBeanTest.pdf";
            //5.通过JasperExportManager管理工具进行报表输出文档，此外设定为输出Html文件
            JasperExportManager.exportReportToPdfFile(jasperPrint, desFilePath);
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
