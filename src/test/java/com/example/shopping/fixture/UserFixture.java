package com.example.shopping.fixture;

import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.Gender;
import com.example.shopping.entity.User;
import com.example.shopping.entity.UserType;

public class UserFixture {
    public static User createUser() {
        return User.builder()
                .id("test")
                .password("test")
                .paymentPassword("111111")
                .userName("test")
                .profileImage("test")
                .gender(Gender.FEMALE)
                .tall((short) 100)
                .weight((short)40)
                .type(UserType.ADMIN)
                .build();
    }
    public static UserDto.Request createUserRequest() {
        return new UserDto.Request(
                "id",
                "password",
                "username",
                "paymentPassword",
                "profileImage",
                Gender.FEMALE,
                (short) 40,
                (short) 100,
                UserType.ADMIN
        );
    }

}
