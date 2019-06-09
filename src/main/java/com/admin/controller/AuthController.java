package com.admin.controller;

import com.admin.common.base.BaseController;
import com.admin.common.bean.ResultBean;
import com.admin.common.exception.AuthException;
import com.admin.common.exception.BaseException;
import com.admin.common.exception.enums.AuthEnum;
import com.admin.model.user.UserDto;
import com.admin.model.user.UserParamDto;
import org.apache.shiro.SecurityUtils;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ZR_a on 2017/12/2.
 */
@RestController
@RequestMapping("auth")
public class AuthController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final long DEFAULT_SESSION_TIMEOUT = 3600 * 1000;

    @RequestMapping(value = "login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean login(@RequestBody UserParamDto userParamDto) {
        String account = userParamDto.getAccount();
        String password = userParamDto.getPassword();
        System.out.println(account);
        System.out.println(password);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        try {
            subject.login(token);
            UserDto user = (UserDto) subject.getPrincipal();
            user.setSid(subject.getSession().getId());
            //remember(true);
            return success(user, "登陆成功");
        } catch (UnknownAccountException e) {
            throw new AuthException(AuthEnum.UNKNOWN_ACCOUNT);
        } catch (DisabledAccountException e) {
            throw new AuthException(AuthEnum.DISABLED_ACCOUNT);
        } catch (IncorrectCredentialsException e) {
            throw new AuthException(AuthEnum.WRONG_PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("异常");
        }

    }

    @RequestMapping(value = "/logout",
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean doLogout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        System.out.println("logout");
        return success(null);
    }

    @RequestMapping(value = "noAuth")
    public String noAuth() {
        /*
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            throw new AuthException(AuthEnum.WRONG_PASSWORD);
        }
       */
        throw new AuthException(AuthEnum.UNAUTHORIZED);
    }

    @RequestMapping(value = "noLogin")
    public String noLogin() {
        throw new AuthException(AuthEnum.UNLOGIN);
    }

    private void remember(boolean isRemember) {
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setTimeout(DEFAULT_SESSION_TIMEOUT);
    }
}
