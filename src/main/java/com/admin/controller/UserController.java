package com.admin.controller;

import com.admin.common.bean.ParamBean;
import com.admin.common.base.BaseController;
import com.admin.common.bean.ResultBean;
import com.admin.model.req.TestRequest;
import com.admin.model.user.UserDto;
import com.admin.service.UserService;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author zhongren
 * @date 2017/11/8
 */
@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBean list() {
        ParamBean paramBean = getParamBean();
        return success(userService.page(paramBean), "分页查询");
    }

    @RequestMapping(value = "create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBean create() {
        UserDto userDto =new UserDto();
        userDto.setAccount("ccc");
        return success(userService.create(userDto), "添加");
    }
    @RequestMapping(value = "test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBean test(@RequestBody @Valid TestRequest testRequest) {
        UserDto userDto =new UserDto();
        userDto.setAccount("ccc");
        return success(userService.create(userDto), "添加");
    }


    public static void main(String[] args) {
        UserDto userDto =new UserDto();
        userDto.setName("1111");
        BeanWrapper beanWrapper=new BeanWrapperImpl(userDto);
        beanWrapper.getPropertyValue("name");
    }
}
