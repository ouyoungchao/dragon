package com.shiliu.dragon.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {


    private static ObjectMapper objectMap = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String toJson(Object object){
        try {
            String json = objectMap.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        if (objectMap == null) {
            objectMap = new ObjectMapper();
        }
        try {
            //字符串转Json对象
            return objectMap.readValue(content, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
