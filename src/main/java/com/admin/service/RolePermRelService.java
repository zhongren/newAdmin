package com.admin.service;


import com.admin.common.base.BaseService;

import com.admin.model.user.UserDto;
import com.admin.repo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhongr on 2017/7/31.
 */
@Service
public class RolePermRelService extends BaseService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public void init() {
        setBaseRepo(userRepo);
    }

    public UserDto findByUserName(String userName) {
        return findBy("user_name", userName, UserDto.class);
    }
}
