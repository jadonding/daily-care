package cn.jdblg.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.TimeZone;

/**
 * @author jadonding
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setTimeZone(TimeZone.getDefault());
    }

    private JsonUtil() {
    }

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }

    public static <T> T from(String value, Class<T> classOfT) {
        try {
            return mapper.readValue(value, classOfT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }

    public static <T> T from(String value, TypeReference<T> valueTypeRef) {
        try {
            return mapper.readValue(value, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理异常", e);
        }
    }
}
