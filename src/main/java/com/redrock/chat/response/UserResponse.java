package com.redrock.chat.response;


import com.redrock.chat.bean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    int status;
    String info;
    String result;
}
