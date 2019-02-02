package com.admin.common.util;

import com.admin.model.user.UserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SysUtil {
    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserDto getLoginUser() {
        UserDto user = (UserDto) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    public static UserDto getPrincipal(){
        Subject subject = SecurityUtils.getSubject() ;
        Object principal = subject.getPrincipal() ;
        return principal == null ? null : (UserDto) principal ;
    }
}
