package com.admin.common.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhongren
 * @date 2017/11/8
 */
public class ParamBean extends HashMap<String, Object> {
    private final static String ASC = "ASC";
    private final static String DESC = "DESC";
    public final static String PAGE_SIZE = "pageSize";
    public final static String PAGE_NUM = "pageNum";
    public final static String ORDER_TYPE = "orderType";
    public final static String ORDER_FIELD = "orderField";
    private Integer pageNum=1;
    private Integer pageSize=20;
    private String orderType;
    private String orderField;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    private Map<String,String> orderMap = new LinkedHashMap<String, String>() ;

    public Map<String, String> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, String> orderMap) {
        this.orderMap = orderMap;
    }

    public ParamBean orderBy(Object field , Object type ){
        if( field == null || type == null ){
            return this;
        }
        return orderBy( field.toString() , type.toString() ) ;
    }

    public String orderBy(){
        if( orderMap.isEmpty() ){
            return null ;
        }
        StringBuffer stringOrderType = new StringBuffer() ;
        for( Entry<String,String> item : orderMap.entrySet() ){
            if( StringUtils.isEmpty( item.getKey() ) || item.getValue() == null ){
                continue ;
            }
            stringOrderType.append( String.format(
                    " %s %s ,", item.getKey()  , item.getValue()) ) ;
        }

        String tempstr = stringOrderType.toString().trim() ;
        if( tempstr.endsWith( "," ) ){
            tempstr = tempstr.substring( 0 , tempstr.length() - 1 ) ;
        }
        return tempstr ;
    }

}
