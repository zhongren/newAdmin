package com.admin.service;

import com.admin.repo.repo.UserRepo;
import com.admin.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhongren
 * @date 2017/11/8
 */
@Service
public class UserService extends BaseService{
    @Autowired
    private UserRepo userRepo;
    @Override
    public void init() {
        setBaseRepo(userRepo);
    }
}
