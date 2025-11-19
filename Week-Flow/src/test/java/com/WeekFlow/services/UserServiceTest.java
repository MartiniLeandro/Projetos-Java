package com.WeekFlow.services;

import com.WeekFlow.entities.User;
import com.WeekFlow.repositories.UserRepository;
import com.WeekFlow.security.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup(){
        user = User.builder().name("user").email("user@email.com").password("user").build();
    }

    @Test
    void testFindUserByToken(){
        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user);
        User user = userService.findUserByToken("Bearer fake-token");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("user",user.getName());
        Assertions.assertEquals("user@email.com",user.getEmail());

        Mockito.verify(userRepository).findUserByEmail("user@email.com");
        Mockito.verify(tokenService).validateToken("fake-token");
    }
}
