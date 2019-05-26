package com.redrock.chat.controller;

import com.redrock.chat.bean.User;
import com.redrock.chat.response.UserResponse;
import com.redrock.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/sign", produces = "application/json")
    public UserResponse regist(User user) {
        if (StringUtils.isEmpty(user.getUsername())) {
            log.error("failed to sign up ");
            return new UserResponse(101, "用户名不能为空", "fail");
        } else if (StringUtils.isEmpty(user.getPassword())) {
            log.error("failed to sign up ");
            return new UserResponse(102, "密码不能为空", "fail");
        } else {
            boolean flag = userService.signUp(user);
            if (flag) {
                return new UserResponse(104, "注册成功", "success");
            } else {
                return new UserResponse(103, "此用户已存在", "fail");
            }
        }

    }

    @PostMapping(value = "/login", produces = "application/json")
    public UserResponse login(User user, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(user.getUsername())) {
            log.error("failed to login");
            return new UserResponse(101, "用户名不能为空", "fail");
        } else if (StringUtils.isEmpty(user.getPassword())) {
            log.error("failed to sign up ");
            return new UserResponse(102, "密码不能为空", "fail");
        } else {
            boolean flag = userService.login(user);
            if (flag) {
                return new UserResponse(103, "登陆成功", "success");
            } else {
                return new UserResponse(104, "用户名或密码错误", "fail");
            }
        }
    }

}

