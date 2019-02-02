package com.admin.common.base;


import com.admin.common.exception.ParamException;
import com.admin.common.exception.enums.ParamEnum;
import com.admin.common.orm.condition.Condition;
import com.admin.common.orm.condition.ConditionMap;
import com.admin.common.orm.condition.Op;
import com.admin.common.orm.condition.UpdateValue;
import com.admin.common.orm.criteria.CreateCriteria;
import com.admin.common.orm.criteria.DeleteCriteria;
import com.admin.common.orm.criteria.SearchCriteria;
import com.admin.common.orm.criteria.UpdateCriteria;
import com.admin.common.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ZR_a on 2017/8/2.
 */
public abstract class BaseRepo {
    private static String PK = "id";
    private static String FIND = "com.admin.common.orm.BaseMapper.find";
    private static String CREATE = "com.admin.common.orm.BaseMapper.create";
    private static String UPDATE = "com.admin.common.orm.BaseMapper.update";
    private static String DELETE = "com.admin.common.orm.BaseMapper.delete";
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    private ConditionMap conditionMap;

    public ConditionMap getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(ConditionMap conditionMap) {
        this.conditionMap = conditionMap;
    }

    @PostConstruct
    public abstract void init();

    /**
     * 多条件查询
     *
     * @param param   条件map(在具体的repo中定义)
     * @param columns
     * @return
     */
    private SearchCriteria createSearchCriteria(Map<String, Object> param, String... columns) {
        Table table = AnnotationUtils.findAnnotation(this.getClass(), Table.class);
        String tableName = table.name();
        SearchCriteria searchCriteria = new SearchCriteria(tableName);
        //添加查询字段
        if (columns != null && columns.length > 0) {
            List<String> columnList = new ArrayList<>();
            columnList.addAll(Arrays.asList(columns));
            searchCriteria.setColumnList(columnList);
        }
        //添加查询条件
        if (conditionMap != null && !conditionMap.isEmpty() && param != null && !param.isEmpty()) {
            List<Condition> conditionList = new ArrayList<>();
            for (Map.Entry<String, Condition> entry : getConditionMap().entrySet()) {
                if (!param.containsKey(entry.getKey())) {
                    continue;
                }
                Condition condition = entry.getValue();
                condition.setValue(param.get(entry.getKey()));
                conditionList.add(condition);
            }
            searchCriteria.setConditionList(conditionList);
        }
        return searchCriteria;
    }

    /**
     * 指定条件查询
     *
     * @param field   条件字段
     * @param value   条件值
     * @param columns
     * @return
     */
    private SearchCriteria createSearchCriteria(String field, Object value, String... columns) {
        Table table = AnnotationUtils.findAnnotation(this.getClass(), Table.class);
        String tableName = table.name();
        SearchCriteria searchCriteria = new SearchCriteria(tableName);
        //添加查询字段
        if (columns != null && columns.length > 0) {
            List<String> columnList = new ArrayList<>();
            columnList.addAll(Arrays.asList(columns));
            searchCriteria.setColumnList(columnList);
        }
        //添加查询条件
        List<Condition> conditionList = new ArrayList<>();
        Condition condition = new Condition(field, Op.EQ, value);
        conditionList.add(condition);
        searchCriteria.setConditionList(conditionList);
        return searchCriteria;
    }

    private CreateCriteria createCreateCriteria(Map<String, Object> param) {
        if (param == null || param.isEmpty()) {
            throw new ParamException(ParamEnum.PARAM_LACK);
        }
        Table table = AnnotationUtils.findAnnotation(this.getClass(), Table.class);
        String tableName = table.name();
        CreateCriteria createCriteria = new CreateCriteria(tableName);
        List<Condition> conditionList = new ArrayList<>();
        for (Map.Entry<String, Object> map : param.entrySet()) {
            if(map.getValue()!=null){
                System.out.println(map.getKey()+"类型"+map.getValue().getClass());
            }

            Condition condition = new Condition(map.getKey(), map.getValue());
            conditionList.add(condition);
        }
        createCriteria.setConditionList(conditionList);
        return createCriteria;
    }

    private UpdateCriteria createUpdateCriteria(Map<String, Object> param, Condition... conditions) {
        if (param == null || param.isEmpty()) {
            throw new ParamException(ParamEnum.PARAM_LACK);
        }
        Table table = AnnotationUtils.findAnnotation(this.getClass(), Table.class);
        String tableName = table.name();
        UpdateCriteria updateCriteria = new UpdateCriteria(tableName);
        List<UpdateValue> updateValueList = new ArrayList<>();
        for (Map.Entry<String, Object> map : param.entrySet()) {
            UpdateValue updateValue = new UpdateValue(map.getKey(), map.getValue());
            updateValueList.add(updateValue);
        }
        updateCriteria.setUpdateValueList(updateValueList);
        if (conditions != null && conditions.length > 0) {
            updateCriteria.setConditionList(Arrays.asList(conditions));
        }
        return updateCriteria;
    }


    /**
     * 删除
     * @param conditions
     * @return
     */

    private DeleteCriteria createDeleteCriteria(Condition... conditions) {
        Table table = AnnotationUtils.findAnnotation(this.getClass(), Table.class);
        String tableName = table.name();
        DeleteCriteria deleteCriteria = new DeleteCriteria(tableName);
        if (conditions != null && conditions.length > 0) {
            deleteCriteria.setConditionList(Arrays.asList(conditions));
        }
        return deleteCriteria;
    }



    /**
     * 查询对象（map）集合
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findMapList(Map<String, Object> param, String... columns) {
        SearchCriteria searchCriteria = createSearchCriteria(param,columns);
        List<Map<String, Object>> data = sqlSessionTemplate.selectList(FIND, searchCriteria);
        for (Map<String, Object> map : data) {
            BeanUtil.translateMapProperty(map);
        }
        return data;
    }

    /**
     * 根据条件查询对象（map）集合
     *
     * @param field
     * @param value
     * @param columns
     * @return
     */
    public List<Map<String, Object>> findMapListBy(String field, Object value, String... columns) {
        SearchCriteria searchCriteria = createSearchCriteria(field, value, columns);
        List<Map<String, Object>> data = sqlSessionTemplate.selectList(FIND, searchCriteria);
        for (Map<String, Object> map : data) {
            BeanUtil.translateMapProperty(map);
        }
        return data;
    }

    /**
     * 根据条件查询对象（map）
     *
     * @param field
     * @param value
     * @param columns
     * @return
     */
    public Map<String, Object> findMapBy(String field, Object value, String... columns) {
        SearchCriteria searchCriteria = createSearchCriteria(field, value, columns);
        List<Map<String, Object>> data = sqlSessionTemplate.selectList(FIND, searchCriteria);
        if (data == null || data.isEmpty()) {
            return null;
        }
        for (Map<String, Object> map : data) {
            BeanUtil.translateMapProperty(map);
        }
        return data.get(0);
    }

    /**
     * 根据条件查询对象（map）,转换为对象（class）
     *
     * @param field
     * @param value
     * @param tClass
     * @param columns
     * @param <T>
     * @return
     */
    public <T> T findBy(String field, Object value, Class<T> tClass, String... columns) {
        Map<String, Object> map = findMapBy(field, value, columns);
        return BeanUtil.convertMap2Bean(map, tClass);
    }

    /**
     *  根据主键ID查询对象（map）,转换为对象（class）
     * @param value
     * @param tClass
     * @param columns
     * @param <T>
     * @return
     */
    public <T> T findById(Object value, Class<T> tClass, String... columns) {
        Map<String, Object> map = findMapBy(PK, value, columns);
        return BeanUtil.convertMap2Bean(map, tClass);
    }

    /**
     * 根据条件查询对象（map）集合,转换为对象集合（class）
     *
     * @param field
     * @param value
     * @param tClass
     * @param columns
     * @param <T>
     * @return
     */
    public <T> List<T> findListBy(String field, Object value, Class<T> tClass, String... columns) {
        List<Map<String, Object>> list = findMapListBy(field, value, columns);
        return BeanUtil.convertMap2List(list, tClass);
    }

    /**
     * 根据条件集合查询对象（map）集合,转换为对象集合（class）
     * @param param
     * @param tClass
     * @param columns
     * @param <T>
     * @return
     */
    public <T> List<T> findListBy(Map<String,Object> param, Class<T> tClass, String... columns) {
        List<Map<String, Object>> list = findMapList(param, columns);
        return BeanUtil.convertMap2List(list, tClass);
    }



    private Object createMap(Map<String, Object> param) {
        CreateCriteria createCriteria = createCreateCriteria(param);
        sqlSessionTemplate.insert(CREATE, createCriteria);
        return createCriteria.getId();
    }

    private Object updateMap(Map<String, Object> param, Condition condition) {
        UpdateCriteria updateCriteria = createUpdateCriteria(param, condition);
        return sqlSessionTemplate.update(UPDATE, updateCriteria);
    }

    /**
     * 创建对象
     *
     * @param bean
     * @param <T>
     * @return
     */
    public <T> Object create(T bean) {
        Map<String, Object> map = BeanUtil.convertBean2Map(bean);
        return createMap(map);
    }

    /**
     * 更新对象
     *
     * @param field    更新的条件字段
     * @param value 更新的条件字段的值
     * @param bean
     * @param <T>
     * @return
     */
    public <T> Object update(String field, Object value, T bean) {
        Map<String, Object> map = BeanUtil.convertBean2Map(bean);
        Condition condition = new Condition(field, Op.EQ, value);
        return updateMap(map, condition);
    }
    private int deleteMap(Condition condition) {
        DeleteCriteria deleteCriteria = createDeleteCriteria(condition);
        return sqlSessionTemplate.delete(DELETE, deleteCriteria);
    }

    public  int delete(String field, Object value) {
        Condition condition = new Condition(field, Op.EQ, value);
        return deleteMap(condition);
    }

}
