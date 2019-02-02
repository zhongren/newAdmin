package com.admin.model.user;

import com.admin.common.base.bean.BaseVo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;

/**
 * @author zhongren
 * @date 2017/11/9
 */

public class UserDto extends BaseVo {
    private String name;
    @JsonIgnore
    private String password;
    private String account;
    private Integer status;
    @JsonIgnore
    private Set<String> permSet;
    private Serializable sid;
    private String roleName;
    private Set<String> roleSet;
    public Serializable getSid() {
        return sid;
    }

    public void setSid(Serializable sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<String> getPermSet() {
        return permSet;
    }

    public void setPermSet(Set<String> permSet) {
        this.permSet = permSet;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", account='" + account + '\'' +
                ", status=" + status +
                ", permSet=" + permSet +
                '}';
    }


}
