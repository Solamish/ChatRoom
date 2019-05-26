package com.redrock.chat.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFriend {

    private String user;
    private String friend;

    /**
     * 好友申请状态
     * 默认为0 （表示申请中或拒绝）
     * 同意好友申请后变为1
     */
    private int status;
}
