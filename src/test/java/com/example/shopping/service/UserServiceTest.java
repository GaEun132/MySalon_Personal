package com.example.shopping.service;


import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.Gender;
import com.example.shopping.entity.User;
import com.example.shopping.entity.UserType;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.fixture.UserFixture;
import com.example.shopping.repository.UserRepository;
import io.jsonwebtoken.security.Password;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    void createUser() {
        //given
        UserDto.Request userDto = UserFixture.createUserRequest();
        // when
        UserDto.Response response =  userService.createUser(userDto);
        Optional<User> savedUser = userRepository.findById(response.getUserId());
        // then
        assertThat(savedUser).isPresent();

        User user = savedUser.get();
        assertThat(savedUser.get().getId()).isEqualTo("id");
        assertThat(user.getUserName()).isEqualTo("username");
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getTall()).isEqualTo((short) 40);
        assertThat(user.getWeight()).isEqualTo((short) 100);
        assertThat(user.getProfileImage()).isEqualTo("profileImage");
        assertThat(user.getType()).isEqualTo(UserType.ADMIN);

        // 비밀번호는 평문 저장이면 안 됨
        assertThat(user.getPassword()).isNotEqualTo("password");
        assertThat(passwordEncoder.matches("password", user.getPassword())).isTrue();


    }

    @Test
    void createDuplicateUser() throws Exception{

        // given
        UserDto.Request userDto1 =  UserFixture.createUserRequest();
        UserDto.Request userDto2 =  UserFixture.createUserRequest();

        // when
        userService.createUser(userDto1);

        // then
        assertThrows(BusinessException.class, () -> userService.createUser(userDto2));

    }

    @Test
    void create() throws Exception{
        String string =  new BCryptPasswordEncoder().encode("111111");
        System.out.println(string);

    }

}