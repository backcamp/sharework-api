package com.sharework.service;

import com.sharework.response.model.SuccessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void checkNickname() {
        String failNickname = "";
        SuccessResponse response = userService.checkNickname(failNickname);
        Assertions.assertFalse(response.getMeta().isStatus());

        failNickname = "1234567";
        response = userService.checkNickname(failNickname);
        Assertions.assertFalse(response.getMeta().isStatus());

        failNickname = "@";
        response = userService.checkNickname(failNickname);
        Assertions.assertFalse(response.getMeta().isStatus());

        failNickname = "테트테스트여";  // Already registered.
        response = userService.checkNickname(failNickname);
        Assertions.assertFalse(response.getMeta().isStatus());

        String passNickname = "강남바리스타";
        response = userService.checkNickname(passNickname);
        Assertions.assertTrue(response.getMeta().isStatus());
    }
}
