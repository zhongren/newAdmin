package com.admin.common.util;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zr on 2017/8/9.
 */
public class BeanUtil extends BeanUtils {


    public static <T> T convertMap2Bean(Map<String, Object> map, Class<T> tClass) {
        if (map == null || map.isEmpty() || tClass == null) {
            return null;
        }

        T t = null;
        try {
            t = tClass.newInstance();
            //translateMapProperty(map);
            populate(t, map);
            cn.hutool.core.bean.BeanUtil.mapToBean(map,tClass,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> convertMap2List(List<Map<String, Object>> list, Class<T> tClass) {
        if (list == null || list.isEmpty() || tClass == null) {
            return null;
        }
        List<T> data = new ArrayList<>();
        for (Map<String, Object> map : list) {
            T t = convertMap2Bean(map, tClass);
            data.add(t);
        }
        return data;
    }

    public static <T> Map<String, Object> convertBean2Map(T bean) {
        if (bean == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                String key = field.getName();
                //保证转换的时候类型不丢失
                Object value = cn.hutool.core.bean.BeanUtil.getProperty(bean, field.getName());
                key=StringUtil.camelToUnderline(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 把xxx_xxx转为xxxXxx的格式
     *
     * @return
     */
    private static String translateBeanProperty(String original) {
        String lowerOriginal = original.toLowerCase();
        return StringUtil.underlineToCamel(lowerOriginal);
    }

    /**
     * 把xxxXxx转为xxx_xxx的格式
     *
     * @return
     */
    private static String reverseTranslateBeanProperty(String original) {
        return StringUtil.camelToUnderline(original);
    }

    public static Map<String, Object> translateMapProperty(Map<String, Object> map) {
        Map<String, Object> tempMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            tempMap.put(translateBeanProperty(entry.getKey()), entry.getValue());
        }
        map.clear();
        map.putAll(tempMap);
        return map;
    }


    public static void main(String[] args) {
        System.out.println(StringUtil.underlineToCamel("user_name_temp"));
    }

}
