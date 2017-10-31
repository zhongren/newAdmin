package com.admin.common.orm.criteria;

import com.admin.common.orm.Condition;

import java.util.List;

/**
 * Created by zr on 2017/8/13.
 */
public class CreateCriteria {
    private String tableName;
    private List<Condition> conditionList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }
}