package com.andycoder.customrpc.version1.service;


import com.andycoder.customrpc.version1.common.User;

public interface UserService {
    // 客户端通过这个接口调用服务端的实现类
    User getUserByUserId(Integer id);
}
