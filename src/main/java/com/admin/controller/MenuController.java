package com.admin.controller;

import com.admin.common.base.BaseController;
import com.admin.common.bean.ResultBean;
import com.admin.common.util.StringUtil;
import com.admin.model.sys.MenuVo;
import com.admin.model.user.UserDto;
import com.admin.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongren
 * @date 2017/11/8
 */
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "menus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBean menus() {
        Boolean build = StringUtil.getBoolean(getParam("build"));
        List<MenuVo> menus = new ArrayList<>();
        UserDto userDto = getLoginUser();
        List<MenuVo> menuVoList = sysService.findUserMenu(userDto.getId());
        if (!menuVoList.isEmpty()) {
            if (build != null && build) {
                for (MenuVo menu1 : menuVoList) {
                    boolean flag = false;
                    for (MenuVo menu : menuVoList) {
                        if (menu1.getParentId().equals(menu.getId())) {
                            menu.getSubMenuVo().add(menu1);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        menus.add(menu1);
                    }
                }
            } else {
                menus=new ArrayList<>(menuVoList);
            }
        }

        return success(menus);
    }
}
