package com.admin.common.util;

import com.admin.common.bean.ParamBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhongren
 * @date 2017/11/8
 */
public class PageUtil {
    public static Page startPage(ParamBean pageParam){
        if (pageParam==null){
            return null;
        }
        Page page=null;
        if (pageParam.getPageNum()>0){
            page = PageHelper.startPage(pageParam.getPageNum(),pageParam.getPageSize());
        }

        if (StringUtils.isNotBlank(pageParam.orderBy())){
            PageHelper.orderBy(pageParam.orderBy());
        }

        return page;
    }
}
