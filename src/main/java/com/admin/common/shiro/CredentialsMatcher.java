package com.admin.common.shiro;


import com.admin.common.util.EncryUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZR_a on 2017/8/25.
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {
    private Logger logger = LoggerFactory.getLogger(CredentialsMatcher.class);
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        //sha-256
        //String encryPassword= EncryUtil.encryPassword(String.valueOf(token.getPassword()));

        //只比较是否相同
        String encryPassword= String.valueOf(token.getPassword());
        token.setPassword(encryPassword.toCharArray());
        //判断密码相同
        return super.doCredentialsMatch(token, authenticationInfo);
    }
}
