<%@ page contentType="application/pdf;charset=UTF-8" %>
<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<%@ page import="net.sf.jasperreports.engine.JasperFillManager" %>--%>
<%@ page import="com.sunjet.frontend.auth.RestClient" %>
<%@ page import="org.springframework.http.HttpEntity" %>
<%@ page import="org.springframework.http.ResponseEntity" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%--<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>--%>
<%--<z:init use="org.zkoss.zkplus.databind.AnnotateDataBinderInit" />--%>
<%
    //    Clients.showBusy("正在生成报表，请稍候...");

    try {
        RestClient restClient = (RestClient) request.getSession().getAttribute("restClient");
        Map<String, Object> map = new HashMap<>();
        map.put("objId", request.getParameter("objId"));
        map.put("printType", request.getParameter("printType"));
        map.put("docNum", request.getParameter("docNum"));
        map.put("dealerPhone", request.getParameter("dealerPhone"));
        map.put("serviceManagerName", request.getParameter("serviceManagerName"));
        ResponseEntity<byte[]> responseEntity = null;
        HttpEntity requestEntity = new HttpEntity<>(map, null);
        responseEntity = restClient.post("/report/printPage", requestEntity, byte[].class);
        byte[] bytes = responseEntity.getBody();
        response.setContentType("application/pdf");
        if (bytes != null) {
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
            outStream.close();
        }

    } catch (IOException e) {
//        e.printStackTrace();
    }


%>