package com.company.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		// 忽略null值
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 时间类型数据格式化
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// java.time时间类型数据格式化
		JavaTimeModule module = new JavaTimeModule();
		module.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		module.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
		module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
		mapper.registerModule(module);
		// 遇未知属性不报错
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 允许使用非双引号属性名字
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 允许一个没有属性的类被序列化
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	private JsonUtil() {
	}

	public static String toJsonString(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("json process error,object:{}", object, e);
		}
		return null;
	}

	public static <T> T toEntity(String jsonString, Class<T> clazz) {
		if (jsonString == null) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (JsonParseException e) {
			logger.error("json parse error,jsonString:{}", jsonString, e);
		} catch (JsonMappingException e) {
			logger.error("json mapping error,jsonString:{}", jsonString, e);
		} catch (IOException e) {
			logger.error("io error,jsonString:{}", jsonString, e);
		}
		return null;
	}

	public static <T> List<T> toList(String jsonString, Class<T> parameterClasses) {
		if (jsonString == null) {
			return Collections.emptyList();
		}
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, parameterClasses);
		try {
			return mapper.readValue(jsonString, javaType);
		} catch (JsonParseException e) {
			logger.error("json parse error,jsonString:{}", jsonString, e);
		} catch (JsonMappingException e) {
			logger.error("json mapping error,jsonString:{}", jsonString, e);
		} catch (IOException e) {
			logger.error("io exception error,jsonString:{}", jsonString, e);
		}
		return Collections.emptyList();
	}

	public static boolean isJsonString(String string) {
		if (StringUtils.isBlank(string)) {
			return false;
		}
		try {
			mapper.readTree(string);
		} catch (Exception e) {
			logger.error("exception error,string:{}", string, e);
			return false;
		}
		return true;
	}

	public static JsonNode toJsonNode(String jsonString) {
		if (jsonString == null) {
			return null;
		}
		try {
			return mapper.readTree(jsonString);
		} catch (IOException e) {
			logger.error("io exception error,jsonString:{}", jsonString, e);
		}
		return null;
	}
	
	public static ObjectNode toNotNullObjectNode(String jsonString) {
		JsonNode jsonNode = toJsonNode(jsonString);
		if (jsonNode == null) {
			return new ObjectNode(mapper.getNodeFactory());
		}
		return (ObjectNode) jsonNode;
	}
	
	public static JsonNode toJsonNode(Object objectValue) {
		return mapper.valueToTree(objectValue);
	}

	public static ObjectMapper mapper() {
		return mapper;
	}

	public static String getString(JsonNode jsonNode, String fieldName) {
		return get(jsonNode, fieldName, JsonNode::asText);
	}

	public static Integer getInteger(JsonNode jsonNode, String fieldName) {
		return get(jsonNode, fieldName, JsonNode::asInt);
	}

	public static boolean getBoolean(JsonNode jsonNode, String fieldName) {
		return get(jsonNode, fieldName, JsonNode::asBoolean);
	}

	public static BigDecimal getBigDecimal(JsonNode jsonNode, String fieldName) {
		return get(jsonNode, fieldName, v -> new BigDecimal(v.asText()));
	}

	private static <V> V get(JsonNode jsonNode, String fieldName, Function<JsonNode, V> function) {
		JsonNode fieldNode = jsonNode.get(fieldName);
		if (fieldNode == null) {
			return null;
		}
		return function.apply(fieldNode);
	}

    /**
     * 仅打印日志使用
     * 1.替换byte[]数组的输出
     * 2.替换长度超过arrMaxLength的数组输出
     *
     * @param object
     * @param arrMaxLength 数组最大长度
     * @return
     */
    public static String toJsonStringReplaceProperties(Object object, int arrMaxLength) {
        if (object == null) {
            return null;
        }
        JsonNode jsonNode = toJsonNode(object);
        replaceProperties(jsonNode, arrMaxLength);
        return toJsonString(jsonNode);
    }

    public static String toJsonStringReplaceProperties(Object object) {
        return toJsonStringReplaceProperties(object, 1000);
    }

    /**
     * 替换属性序列化(递归)
     */
    private static void replaceProperties(JsonNode jsonNode, int arrMaxLength) {
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                replaceProperties(node, arrMaxLength);
            }
            return;
        }
        Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            JsonNode value = entry.getValue();
            if (value.isBinary()) {// 移除byte[]属性
                ObjectNode objectNode = (ObjectNode) jsonNode;
                objectNode.put(entry.getKey(), "binary data...");
            } else if (value.size() > arrMaxLength) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                objectNode.put(entry.getKey(), "data too long...");
            } else {
                replaceProperties(value, arrMaxLength);// 递归处理子节点
            }
        }
    }
}
