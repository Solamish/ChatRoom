package com.redrock.chat.service;

import com.redrock.chat.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    @Autowired
    FriendMapper friendMapper;

    /**
     * 好友申请
     */
    public boolean apply(String currentUser, String friendName) {
        return friendMapper.apply(currentUser, friendName);
    }

    /**
     * 好友申请状态，便于以后查询我的好友
     */
    public boolean checkStaus(String currentUser, String friendName) {
        return friendMapper.checkStatus(currentUser, friendName);
    }

    public int getID(String currentUser, String friendName) {
        return friendMapper.getID(currentUser, friendName);
    }

    /**
     * 同意申请
     */
    public void agree(int id) {
        friendMapper.agree(id);
    }

    /**
     * 添加好友
     */
    public boolean followFriend(String currentUser, String friendName) {
        return friendMapper.followFriend(currentUser, friendName);
    }
}
