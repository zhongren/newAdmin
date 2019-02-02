package com.admin.common.shiro;


import com.admin.SysConstants;
import com.admin.common.util.ApplicationContextUtil;
import com.admin.model.sys.PermPo;
import com.admin.model.user.UserDto;
import com.admin.service.SysService;
import com.admin.service.UserService;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ZR_a on 2017/8/24.
 */
public class ShiroRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);


    /**
     * 权限
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDto userDto = (UserDto) principalCollection.getPrimaryPrincipal();
        return getAuthrizationInfo(userDto);
    }

    /**
     * 登录
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String account = token.getUsername();
        UserDto userDto = ApplicationContextUtil.getBean(UserService.class).findByAccount(account);
        if (userDto == null) {
            //用户不存在
            throw new UnknownAccountException();
        }
        if (userDto.getStatus().equals(SysConstants.USER_STATUS_UNACTIVE)) {
            //用户被禁用
            throw new DisabledAccountException();
        }
        getAuthrizationInfo(userDto);
        return new SimpleAuthenticationInfo(userDto, userDto.getPassword(), getName());
    }

    /**
     * 设置自定义密码匹配验证器（在application-shiro.xml中注入）
     *
     * @param myCredentialsMatcher
     */
    public void setCredentialsMatcher(CredentialsMatcher myCredentialsMatcher) {
        super.setCredentialsMatcher(myCredentialsMatcher);
    }

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    private Set<String> findUserPerm(Long userId) {
        SysService sysService = ApplicationContextUtil.getBean(SysService.class);
        List<PermPo> permPoList = sysService.findUserPerm(userId);
        Set<String> userPerms = new HashSet<>();
        if (permPoList != null && !permPoList.isEmpty()) {
            for (PermPo permPo : permPoList) {
                String permUrl = permPo.getUrl();
                String menuUrl = permPo.getMenuUrl();
                String perm = menuUrl + ":" + permUrl;
                userPerms.add(perm);
            }
        }
        return userPerms;
    }

    private AuthorizationInfo getAuthrizationInfo(UserDto userDto) {
        SimpleAuthorizationInfo authorization = new SimpleAuthorizationInfo();
        if (userDto.getPermSet() != null) {
            authorization.setStringPermissions(userDto.getPermSet());
        } else {
            Set<String> permSet = findUserPerm(userDto.getId());
            authorization.setStringPermissions(permSet);
            userDto.setPermSet(authorization.getStringPermissions());
        }
        return authorization;
    }

}
