package com.redrock.chat.service;


import com.redrock.chat.bean.User;
import com.redrock.chat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public boolean signUp(User user) {
        String username = user.getUsername();
        User uzer = userMapper.findByName(username);
        if (uzer == null) {
            return userMapper.insertUser(user);
        }
        return false;
    }

    public boolean login(User user) {

        return userMapper.checkUser(user) != null;
    }
}
