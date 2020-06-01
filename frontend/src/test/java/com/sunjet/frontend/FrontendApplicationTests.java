package com.sunjet.frontend;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.User;
import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.auth.AuthHelper;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.basic.MaintainTypeService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.common.WordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FrontendApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestClient restClient;

    @Autowired
    private UserService userService;
    @Autowired
    WarrantyMaintenanceService warrantyMaintenanceService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void contextLoads() {
    }

    @Test
    public void restClient04() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("CUSTOM_IP", "127.0.0.1");
        headers.add("TOKEN", "abcdefg");

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);

        //userService.httpGet("http://localhost:9000/user/admin");

//        List<Product> list = (List<Product>) restTemplate.getForObject(URL, List.class);
        ResponseEntity<User> responseEntity = restTemplate.exchange(String.format("http://localhost:9000/user/admin"), HttpMethod.GET, AuthHelper.getHttpEntity(), User.class);
//        List<Product> list = (List<Product>)entity.getBody();
        User user = responseEntity.getBody();
        log.info(user.toString());
        String json = objectMapper.writeValueAsString(user);
        log.info(json);
    }


    /**
     * 返回单个对象
     */
    @Test
    public void restClient05() {
        ResponseEntity<User> responseEntity = restClient.get("/user/admin", User.class);
        log.info(responseEntity.getBody().toString());
    }

    /**
     * 返回对象列表
     */
    @Test
    public void restClient06() {
        ResponseEntity<List> responseEntity = restClient.get("/users", List.class);
        log.info(String.valueOf(responseEntity.getBody().size()));
    }

    /**
     * 返回json对象
     */
    @Test
    public void restClient07() {
        ResponseEntity<String> responseEntity = restClient.get("/user/admin", String.class);
        log.info(responseEntity.getBody().toString());
    }

    /**
     * 返回对象列表的json对象
     */
    @Test
    public void restClient08() {
        ResponseEntity<String> responseEntity = restClient.get("/users", String.class);
        log.info(String.valueOf(responseEntity.getBody()));
    }

    @Test
    public void restClient09() {
        ResponseEntity<User> responseEntity = null;
        try {
            User user = User.builder().logId("aaa").name("aaa").build();
            HttpEntity<User> requestEntity = new HttpEntity<>(user, null);
            responseEntity = restClient.post("/user/add2", requestEntity, User.class);
            log.info(responseEntity.getStatusCode().toString());
            log.info(requestEntity.getBody().toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info(responseEntity.getStatusCode().toString());
        }
    }

    @Test
    public void baseSave() {
        WarrantyMaintenanceInfo oneById = warrantyMaintenanceService.findOneById("8a9982485d50320f015d62e3eba06ddd");
        FlowDocInfo flowDocInfo = oneById;
        ResponseEntity<Map> responseEntity = null;
        flowDocInfo.setStatus(3);
        HttpEntity requestEntity = new HttpEntity<>(flowDocInfo, null);
        responseEntity = restClient.post("/base/save", requestEntity, Map.class);
        responseEntity.getBody();


    }

    @Test
    public void java2word() {
        try {

            File templateFile = new File("D://report.doc");
            insertDataToTable("D://report.doc", 3, false);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void insertDataToTable(String filePath, int tableSize, boolean isDelTmpRow) throws Exception {
        InputStream is = new FileInputStream(filePath);
        XWPFDocument doc = new XWPFDocument(is);
        List<List<String>> resultList = generateTestData(4);
        insertValueToTable(doc, resultList, tableSize, isDelTmpRow);
        saveDocument(doc, "f:/saveFile/temp/sys_" + System.currentTimeMillis() + ".docx");
    }

    //生成测试数据
    public List<List<String>> generateTestData(int num) {
        List<List<String>> resultList = new ArrayList<List<String>>();
        for (int i = 1; i <= num; i++) {
            List<String> list = new ArrayList<String>();
            list.add("" + i);
            list.add("测试_" + i);
            list.add("测试2_" + i);
            list.add("测试3_" + i);
            list.add("测试4_" + i);
            resultList.add(list);
        }
        return resultList;
    }


    /**
     * @param resultList   填充数据
     * @param tableRowSize 模版表格行数 取第一个行数相等列数相等的表格填充
     * @param isDelTmpRow  是否删除模版行
     * @Description: 按模版行样式填充数据, 暂未实现特殊样式填充(如列合并)，只能用于普通样式(如段落间距 缩进 字体 对齐)
     */
    //TODO 数据行插到模版行下面，没有实现指定位置插入
    public void insertValueToTable(XWPFDocument doc, List<List<String>> resultList, int tableRowSize, boolean isDelTmpRow) throws Exception {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table = null;
        List<XWPFTableRow> rows = null;
        List<XWPFTableCell> cells = null;
        List<XWPFTableCell> tmpCells = null;//模版列
        XWPFTableRow tmpRow = null;//匹配用
        XWPFTableCell tmpCell = null;//匹配用
        boolean flag = false;//是否找到表格
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            if (rows.size() == tableRowSize) {
                tmpRow = rows.get(tableRowSize - 1);
                cells = tmpRow.getTableCells();
                if (cells.size() == resultList.get(0).size()) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            return;
        }
        tmpCells = tmpRow.getTableCells();
        for (int i = 0, len = resultList.size(); i < len; i++) {
            XWPFTableRow row = table.createRow();
            row.setHeight(tmpRow.getHeight());
            List<String> list = resultList.get(i);
            cells = row.getTableCells();
            //插入的行会填充与表格第一行相同的列数
            for (int k = 0, klen = cells.size(); k < klen; k++) {
                tmpCell = tmpCells.get(k);
                XWPFTableCell cell = cells.get(k);
                setCellText(tmpCell, cell, list.get(k));
            }
            //继续写剩余的列
            for (int j = cells.size(), jlen = list.size(); j < jlen; j++) {
                tmpCell = tmpCells.get(j);
                XWPFTableCell cell = row.addNewTableCell();
                setCellText(tmpCell, cell, list.get(j));
            }
        }
        //删除模版行
        if (isDelTmpRow) {
            table.removeRow(tableRowSize - 1);
        }
    }

    public void setCellText(XWPFTableCell tmpCell, XWPFTableCell cell, String text) throws Exception {
        CTTc cttc2 = tmpCell.getCTTc();
        CTTcPr ctPr2 = cttc2.getTcPr();

        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        cell.setColor(tmpCell.getColor());
        cell.setVerticalAlignment(tmpCell.getVerticalAlignment());
        if (ctPr2.getTcW() != null) {
            ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
        }
        if (ctPr2.getVAlign() != null) {
            ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
        }
        if (cttc2.getPList().size() > 0) {
            CTP ctp = cttc2.getPList().get(0);
            if (ctp.getPPr() != null) {
                if (ctp.getPPr().getJc() != null) {
                    cttc.getPList().get(0).addNewPPr().addNewJc().setVal(ctp.getPPr().getJc().getVal());
                }
            }
        }

        if (ctPr2.getTcBorders() != null) {
            ctPr.setTcBorders(ctPr2.getTcBorders());
        }

        XWPFParagraph tmpP = tmpCell.getParagraphs().get(0);
        XWPFParagraph cellP = cell.getParagraphs().get(0);
        XWPFRun tmpR = null;
        if (tmpP.getRuns() != null && tmpP.getRuns().size() > 0) {
            tmpR = tmpP.getRuns().get(0);
        }
        XWPFRun cellR = cellP.createRun();
        cellR.setText(text);
        //复制字体信息
        if (tmpR != null) {
            cellR.setBold(tmpR.isBold());
            cellR.setItalic(tmpR.isItalic());
            cellR.setStrike(tmpR.isStrike());
            cellR.setUnderline(tmpR.getUnderline());
            cellR.setColor(tmpR.getColor());
            cellR.setTextPosition(tmpR.getTextPosition());
            if (tmpR.getFontSize() != -1) {
                cellR.setFontSize(tmpR.getFontSize());
            }
            if (tmpR.getFontFamily() != null) {
                cellR.setFontFamily(tmpR.getFontFamily());
            }
            if (tmpR.getCTR() != null) {
                if (tmpR.getCTR().isSetRPr()) {
                    CTRPr tmpRPr = tmpR.getCTR().getRPr();
                    if (tmpRPr.isSetRFonts()) {
                        CTFonts tmpFonts = tmpRPr.getRFonts();
                        CTRPr cellRPr = cellR.getCTR().isSetRPr() ? cellR.getCTR().getRPr() : cellR.getCTR().addNewRPr();
                        CTFonts cellFonts = cellRPr.isSetRFonts() ? cellRPr.getRFonts() : cellRPr.addNewRFonts();
                        cellFonts.setAscii(tmpFonts.getAscii());
                        cellFonts.setAsciiTheme(tmpFonts.getAsciiTheme());
                        cellFonts.setCs(tmpFonts.getCs());
                        cellFonts.setCstheme(tmpFonts.getCstheme());
                        cellFonts.setEastAsia(tmpFonts.getEastAsia());
                        cellFonts.setEastAsiaTheme(tmpFonts.getEastAsiaTheme());
                        cellFonts.setHAnsi(tmpFonts.getHAnsi());
                        cellFonts.setHAnsiTheme(tmpFonts.getHAnsiTheme());
                    }
                }
            }
        }
        //复制段落信息
        cellP.setAlignment(tmpP.getAlignment());
        cellP.setVerticalAlignment(tmpP.getVerticalAlignment());
        cellP.setBorderBetween(tmpP.getBorderBetween());
        cellP.setBorderBottom(tmpP.getBorderBottom());
        cellP.setBorderLeft(tmpP.getBorderLeft());
        cellP.setBorderRight(tmpP.getBorderRight());
        cellP.setBorderTop(tmpP.getBorderTop());
        cellP.setPageBreak(tmpP.isPageBreak());
        if (tmpP.getCTP() != null) {
            if (tmpP.getCTP().getPPr() != null) {
                CTPPr tmpPPr = tmpP.getCTP().getPPr();
                CTPPr cellPPr = cellP.getCTP().getPPr() != null ? cellP.getCTP().getPPr() : cellP.getCTP().addNewPPr();
                //复制段落间距信息
                CTSpacing tmpSpacing = tmpPPr.getSpacing();
                if (tmpSpacing != null) {
                    CTSpacing cellSpacing = cellPPr.getSpacing() != null ? cellPPr.getSpacing() : cellPPr.addNewSpacing();
                    if (tmpSpacing.getAfter() != null) {
                        cellSpacing.setAfter(tmpSpacing.getAfter());
                    }
                    if (tmpSpacing.getAfterAutospacing() != null) {
                        cellSpacing.setAfterAutospacing(tmpSpacing.getAfterAutospacing());
                    }
                    if (tmpSpacing.getAfterLines() != null) {
                        cellSpacing.setAfterLines(tmpSpacing.getAfterLines());
                    }
                    if (tmpSpacing.getBefore() != null) {
                        cellSpacing.setBefore(tmpSpacing.getBefore());
                    }
                    if (tmpSpacing.getBeforeAutospacing() != null) {
                        cellSpacing.setBeforeAutospacing(tmpSpacing.getBeforeAutospacing());
                    }
                    if (tmpSpacing.getBeforeLines() != null) {
                        cellSpacing.setBeforeLines(tmpSpacing.getBeforeLines());
                    }
                    if (tmpSpacing.getLine() != null) {
                        cellSpacing.setLine(tmpSpacing.getLine());
                    }
                    if (tmpSpacing.getLineRule() != null) {
                        cellSpacing.setLineRule(tmpSpacing.getLineRule());
                    }
                }
                //复制段落缩进信息
                CTInd tmpInd = tmpPPr.getInd();
                if (tmpInd != null) {
                    CTInd cellInd = cellPPr.getInd() != null ? cellPPr.getInd() : cellPPr.addNewInd();
                    if (tmpInd.getFirstLine() != null) {
                        cellInd.setFirstLine(tmpInd.getFirstLine());
                    }
                    if (tmpInd.getFirstLineChars() != null) {
                        cellInd.setFirstLineChars(tmpInd.getFirstLineChars());
                    }
                    if (tmpInd.getHanging() != null) {
                        cellInd.setHanging(tmpInd.getHanging());
                    }
                    if (tmpInd.getHangingChars() != null) {
                        cellInd.setHangingChars(tmpInd.getHangingChars());
                    }
                    if (tmpInd.getLeft() != null) {
                        cellInd.setLeft(tmpInd.getLeft());
                    }
                    if (tmpInd.getLeftChars() != null) {
                        cellInd.setLeftChars(tmpInd.getLeftChars());
                    }
                    if (tmpInd.getRight() != null) {
                        cellInd.setRight(tmpInd.getRight());
                    }
                    if (tmpInd.getRightChars() != null) {
                        cellInd.setRightChars(tmpInd.getRightChars());
                    }
                }
            }
        }
    }


    /**
     * 导出数据
     *
     * @param document
     * @param savePath
     * @throws Exception
     */
    public void saveDocument(XWPFDocument document, String savePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(savePath);
        document.write(fos);
        fos.close();
    }


    @Test
    public void createDoc() throws IOException, XmlException {
        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File("D://create_table.docx"));


        //添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        //设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraphRun.setText("质量费用速报");
        titleParagraphRun.setColor("000000");
        titleParagraphRun.setFontSize(20);

        //工作经历表格
        XWPFTable ComTable = document.createTable(3, 4);
        XWPFTableRow tableRowOne = ComTable.getRow(0);
        tableRowOne.getCell(0).setText("col one, row one");
        tableRowOne.addNewTableCell().setText("col two, row one");
        tableRowOne.addNewTableCell().setText("col three, row one");
        //create second row
        XWPFTableRow tableRowTwo = ComTable.createRow();
        tableRowTwo.getCell(0).setText("col one, row two");
        tableRowTwo.getCell(1).setText("col two, row two");
        tableRowTwo.getCell(2).setText("col three, row two");
        //create third row
        XWPFTableRow tableRowThree = ComTable.createRow();
        tableRowThree.getCell(0).setText("col one, row three");
        tableRowThree.getCell(1).setText("col two, row three");
        tableRowThree.getCell(2).setText("col three, row three");

        //
        ////列宽自动分割
        //CTTblWidth comTableWidth = ComTable.getCTTbl().addNewTblPr().addNewTblW();
        //comTableWidth.setType(STTblWidth.DXA);
        //comTableWidth.setW(BigInteger.valueOf(9072));
        //
        ////表格第一行
        //XWPFTableRow comTableRowOne = ComTable.getRow(0);
        //comTableRowOne.getCell(0).setText("开始时间");
        //comTableRowOne.addNewTableCell().setText("结束时间");
        //comTableRowOne.addNewTableCell().setText("公司名称");
        //comTableRowOne.addNewTableCell().setText("title");
        //
        ////表格第二行
        //XWPFTableRow comTableRowTwo = ComTable.createRow();
        //comTableRowTwo.getCell(0).setText("2016-09-06");
        //comTableRowTwo.getCell(1).setText("至今");
        //comTableRowTwo.getCell(2).setText("seawater");
        //comTableRowTwo.getCell(3).setText("Java开发工程师");
        //
        ////表格第三行
        //XWPFTableRow comTableRowThree = ComTable.createRow();
        //comTableRowThree.getCell(0).setText("2016-09-06");
        //comTableRowThree.getCell(1).setText("至今");
        //comTableRowThree.getCell(2).setText("seawater");
        //comTableRowThree.getCell(3).setText("Java开发工程师");


        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        //添加页眉
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        String headerText = "Java POI create MS word file.";
        ctHeader.setStringValue(headerText);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
        //设置为右对齐
        headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);


        //添加页脚
        CTP ctpFooter = CTP.Factory.newInstance();
        CTR ctrFooter = ctpFooter.addNewR();
        CTText ctFooter = ctrFooter.addNewT();
        String footerText = "http://blog.csdn.net/zhouseawater";
        ctFooter.setStringValue(footerText);
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
        parsFooter[0] = footerParagraph;
        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);

        document.write(out);
        out.close();
        System.out.println("create_table document written success.");
    }


    @Test
    public void createDoc2() throws FileNotFoundException {
        /**
         * 获取请求参数
         */
        String pzjg = "11"; // 篇章结构
        String gdnr = "gd22nr"; // 观点内容
        String jsyy = "jsyy"; // 句式运用
        String chyf = "chyf"; // 词汇语法
        String xzgf = "xzgf"; // 写作规范

        // 获取应用的根路径
        //String servletContextRealPath = request.getServletContext().getRealPath("");
        // 获取模板文件
        File templateFile = new File("D://report.dot");

        ByteArrayOutputStream ostream = null;
        try {
            FileInputStream in = new FileInputStream(templateFile);
            HWPFDocument hwpfDocument = new HWPFDocument(in);
            // 替换读取到的 word 模板内容的指定字段
            Map<String, String> params = new HashMap<>();
            params.put("$PZJG$", pzjg);
            params.put("$GDNR$", gdnr);
            params.put("$JSYY$", jsyy);
            params.put("$CHYF$", chyf);
            params.put("$XZGF$", xzgf);
            Range range = hwpfDocument.getRange();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }


            // 随机生成一个文件名
            UUID randomUUID = UUID.randomUUID();
            String attachmentName = randomUUID.toString();

            //response.addHeader("Content-Disposition", "attachment; filename=\""+ attachmentName + ".doc\"");
            FileOutputStream out = new FileOutputStream(new File("D://create_table.docx"));
            ostream = new ByteArrayOutputStream();
            hwpfDocument.write(ostream);
            out.write(ostream.toByteArray());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runTest() {
        String inputUrl = "/Users/lhj/001.docx";
        //解析docx模板并获取document对象
        try {
            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
            //获取整个文本对象
            List<XWPFParagraph> allParagraph = document.getParagraphs();

            //获取XWPFRun对象输出整个文本内容
            StringBuffer tempText = new StringBuffer();
            for (XWPFParagraph xwpfParagraph : allParagraph) {
                List<XWPFRun> runList = xwpfParagraph.getRuns();
                for (XWPFRun xwpfRun : runList) {
                    tempText.append(xwpfRun.toString());
                }
            }
            System.out.println(tempText.toString());


            StringBuffer tableText = new StringBuffer();

            //解析docx模板并获取document对象
//            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
            //获取全部表格对象
            List<XWPFTable> allTable = document.getTables();

            for (XWPFTable xwpfTable : allTable) {
                //获取表格行数据
                List<XWPFTableRow> rows = xwpfTable.getRows();
                for (XWPFTableRow xwpfTableRow : rows) {
                    //获取表格单元格数据
                    List<XWPFTableCell> cells = xwpfTableRow.getTableCells();
                    for (XWPFTableCell xwpfTableCell : cells) {
                        List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
                        for (XWPFParagraph xwpfParagraph : paragraphs) {
                            List<XWPFRun> runs = xwpfParagraph.getRuns();
                            for (int i = 0; i < runs.size(); i++) {
                                XWPFRun run = runs.get(i);
                                tableText.append(run.toString());
                            }
                        }
                    }
                }
            }
            System.out.println(tableText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wordWriteTest() {
        String inputUrl = "/Users/lhj/001.docx";
        String outputUrl = "/Users/lhj/001-1.docx";

        try {
            //获取word文档解析对象
            XWPFDocument doucument = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
            //获取段落文本对象
            List<XWPFParagraph> paragraph = doucument.getParagraphs();
            //获取首行run对象
            XWPFRun run = paragraph.get(0).getRuns().get(0);
            //设置文本内容
            run.setText("修改了的word");
            //生成新的word
            File file = new File(outputUrl);

            FileOutputStream stream = new FileOutputStream(file);
            doucument.write(stream);
            stream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void writeWordByTemplateTest() {
        //模板文件地址
        String inputUrl = "F://report.docx";
        //新生产的模板文件
        String outputUrl = "F://report-gen.docx";

        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("title", "质量速报");
        testMap.put("docNo", "ZLSB201707210003");
        testMap.put("submitterName", "李建明");
        testMap.put("submitterNamePhone", "13423544335");
        testMap.put("serviceManager", "韦劵波");
        testMap.put("serviceManagerPhone", "15877278685");

        List<String[]> testList = new ArrayList<String[]>();
        testList.add(new String[]{"1", "vin000", "vsn000", "", "", "", "", "", ""});
        testList.add(new String[]{"2", "vin000", "vsn000", "", "", "", "", "", ""});
        testList.add(new String[]{"3", "vin000", "vsn000", "", "", "", "", "", ""});
        testList.add(new String[]{"4", "vin000", "vsn000", "", "", "", "", "", ""});
        testList.add(new String[]{"5", "vin000", "vsn000", "", "", "", "", "", ""});
        testList.add(new String[]{"6", "vin000", "vsn000", "", "", "", "", "", ""});


        WordUtil.changWord(inputUrl, outputUrl, testMap, testList);
    }

    @Test
    public void testPoih() {
        try {
            XWPFTemplate template = XWPFTemplate.compile("D://PB.docx").render(new HashMap<String, Object>() {{
                put("title", new TextRenderData("安徽省Q400颍州区小天使幼儿园监控主机损坏10FK303297"));
                put("docNo", new TextRenderData("ZLSB201710250006"));
                put("submitterName", new TextRenderData("穆可勇"));
                put("submitterPhone", new TextRenderData("0558-2665050"));
                put("dealerCode", new TextRenderData("2632029"));
                put("dealerName", new TextRenderData("安徽省阜阳市裕安汽车销售有限公司"));
                put("serviceManager", new TextRenderData("曾维任"));
                put("serviceManagerPhone", new TextRenderData("18277252350"));
                put("table", new TableRenderData(new ArrayList<RenderData>() {{
                    add(new TextRenderData("d0d0d0", ""));
                    add(new TextRenderData("d0d0d0", "introduce"));
                    add(new TextRenderData("d0d0d0", "introduce"));
                }}, new ArrayList<Object>() {{
                    add("1;add new; # gramer");
                    add("2;support; insert table");
                    add("3;support; more style");
                    add("4;support;more style");
                    add("4");
                    add("5;support;more style");
                    add("1;LGLAA2B1XFK301654;ML0500000CALF40000;GL6507XQ;颍州区宝宝乐幼儿园;29335;1439568000000;1500597414000");
                    add("6");
                }}, "no datas", 10600));

            }});

            FileOutputStream out = new FileOutputStream("D://out.docx");
            template.write(out);
            template.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private MaintainTypeService maintainTypeService;

    @Test
    public void ImportData() {

    }


    @Test
    public void re() {
        List<Map<String, String>> list = new ArrayList<>();
        List<String> detectorData = new ArrayList<>();
        detectorData.add("dump_exception_data_detector_status");
        detectorData.add("source_exception_data_detector_status");
        detectorData.add("dump_expired_data_detector_status");
        detectorData.add("source_expired_data_detector_status");
        detectorData.forEach(e -> {
            Map<String, String> map = new HashMap<>();
            map.put("statusKey", e);
            String[] split = e.split("_");
            map.put("repository", split[0]);
            map.put("type", split[1]);
            list.add(map);
        });

        list.forEach(
                e -> {
                    e.forEach((key, value) -> {
                        System.out.println(value);
                    });
                }
        );


    }
}
