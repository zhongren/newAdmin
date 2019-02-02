package com.admin.common.bean;

import com.admin.common.base.bean.BaseBean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

public class ResultBean implements Serializable {
    private static final long serialVersionUID = 1L;
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 状态码
    private String code;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    // 通用状态码
    public static String SUCCESS = "1";
    public static String FAIL = "0";
    public static String NO_AUTH = "-999";

    public static ResultBean build(String status, String msg, Object data) {
        return new ResultBean(status, msg, data);
    }

    public static ResultBean success(Object data) {
        return new ResultBean(data);
    }

    public static ResultBean success(String msg, Object data) {
        return build(SUCCESS, msg, data);
    }

    public static ResultBean success() {
        return new ResultBean(null);
    }

    public static ResultBean fail(String msg, Object data) {
        return build(FAIL, msg, data);
    }


    public ResultBean() {

    }

    public ResultBean(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ResultBean(Object data) {
        this.code = SUCCESS;
        this.msg = "OK";
        this.data = data;
    }

    public ResultBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为ResultModel对象
     *
     * @param jsonData json数据
     * @param clazz    TaotaoResult中的object类型
     * @return
     */
    public static ResultBean formatToBean(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ResultBean.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isObject()) {
                obj = MAPPER.readValue(data.traverse(), clazz);
            } else if (data.isTextual()) {
                obj = MAPPER.readValue(data.asText(), clazz);
            }
            return build(jsonNode.get("code").asText(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static ResultBean format(String json) {
        try {
            return MAPPER.readValue(json, ResultBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz    集合中的类型
     * @return
     */
    public static ResultBean formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").asText(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
