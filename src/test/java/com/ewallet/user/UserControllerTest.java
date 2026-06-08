package com.ewallet.user;

import com.ewallet.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    @Test
    void login_ShouldReturnTokenSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setName("Shivam");
        user.setEmail("shivam@test.com");
        user.setRole(Role.USER);

        UserController.LoginRequest request =
                new UserController.LoginRequest();

        request.email = "shivam@test.com";
        request.password = "123456";

        when(userService.loginUser(
                "shivam@test.com",
                "123456"))
                .thenReturn(user);

        when(jwtUtil.generateToken(
                "shivam@test.com",
                "USER"))
                .thenReturn("test-jwt-token");

        ResponseEntity<Map<String, Object>> response =
                userController.login(request);

        assertEquals(200, response.getStatusCode().value());

        assertEquals(
                "test-jwt-token",
                response.getBody().get("token")
        );

        verify(userService, times(1))
                .loginUser(
                        "shivam@test.com",
                        "123456"
                );
    }
}