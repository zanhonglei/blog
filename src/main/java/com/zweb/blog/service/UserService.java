package com.zweb.blog.service;

import com.zweb.blog.entity.User;

import java.util.List;

public interface UserService {

    int addUser(User user);

    List<User> findAllUser(int pageNum, int pageSize);


}

