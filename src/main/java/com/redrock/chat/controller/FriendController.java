package com.redrock.chat.controller;

import com.redrock.chat.bean.User;
import com.redrock.chat.response.UserResponse;
import com.redrock.chat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @GetMapping("/apply")
    public UserResponse apply(HttpSession session, String friendName) {
        String username = (String) session.getAttribute("username");
        boolean flag = friendService.apply(username, friendName);
        if (flag) {
            return new UserResponse(101, "提交好友申请成功", "success");
        } else  {
            return new UserResponse(102, "提交好友申请失败", "fail");
        }
    }

    @GetMapping("/agree")
    public UserResponse agree(HttpSession session, String friendName) {
        String username = (String) session.getAttribute("username");
        boolean isNotFriend = friendService.checkStaus(username, friendName);
        int id = friendService.getID(username, friendName);
        if (isNotFriend) {
            friendService.agree(id);
            boolean flag = friendService.followFriend(username, friendName);
            if (flag) {
                return new UserResponse(101, "添加成功", "success");
            } else {
                return new UserResponse(102, "添加失败", "fail");
            }
        } else {
            return new UserResponse(103, "你们已经是好友啦，请勿重复添加", "fail");
        }
    }

    @GetMapping("/disagree")
    public UserResponse disagree(HttpSession session, String friendName) {
        String username = (String) session.getAttribute("username");
        boolean isNotFriend = friendService.checkStaus(username, friendName);
        if (!isNotFriend) {
            return new UserResponse(103, "你们已经是好友啦，请勿重复添加", "fail");
        } else {
            return new UserResponse(101, "您已拒绝对方的好友申请", "success");
        }
    }
}
