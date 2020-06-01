package com.sunjet.utils.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by lhj on 16/10/19.
 * 2016-11-28  lhj  解决json中文乱码
 */
public class JsonHelper {

    /**
     * Bean转Json
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static String bean2Json(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

//        objectMapper= new ObjectMapper();
        //当找不到对应的序列化器时 忽略此字段
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //使Jackson JSON支持Unicode编码非ASCII字符
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StringUnicodeSerializer());
        objectMapper.registerModule(module);
        //设置null值不参与序列化(字段不被显示)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //支持结束
//        mapper.con
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        objectMapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }

    /**
     * Json转Bean
     *
     * @param jsonStr
     * @param objClass
     * @param <T>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();

//        objectMapper= new ObjectMapper();
        //当找不到对应的序列化器时 忽略此字段
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //使Jackson JSON支持Unicode编码非ASCII字符
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StringUnicodeSerializer());
        objectMapper.registerModule(module);
        //设置null值不参与序列化(字段不被显示)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.readValue(jsonStr, objClass);
    }


    public static <T> T map2Bean(Object info, Class<T> objClass) throws JsonParseException, JsonMappingException, IOException {

        JSONObject jsonObject = JSONObject.fromObject(info);

        return JsonHelper.json2Bean(jsonObject.toString(), objClass);
    }
}
