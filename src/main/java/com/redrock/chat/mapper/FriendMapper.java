package com.redrock.chat.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FriendMapper {

    @Insert("Insert into addfriend (user, friend, status) value (#{user}, #{friend}, 0)")
    boolean apply(@Param("user") String currentUser, @Param("fiend") String friendName);

    @Select("Select * from addfriend where user = #{user}, friend = #{friend} and status = 0")
    boolean checkStatus(@Param("user") String currentUser, @Param("fiend") String friendName);

    @Select("Select COUNT(id) from addfriend where user = #{user} and friend = #{friend}")
    int getID(@Param("user") String currentUser, @Param("fiend") String friendName);

    @Update("Update addfriend SET status = 1 where id = #{id}")
    void agree(@Param("id") int id);

    @Insert("Insert into friend (user, friend) value (#{user}, #{friend})")
    boolean followFriend(@Param("user") String currentUser, @Param("fiend") String friendName);


}
