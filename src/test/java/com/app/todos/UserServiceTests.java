package com.app.todos;

import com.app.todos.adapters.input.users.UserRequestDTO;
import com.app.todos.adapters.input.users.UserUpdateDTO;
import com.app.todos.application.gateway.security.TokenGateway;
import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.application.usecases.security.PasswordUseCase;
import com.app.todos.domain.users.User;
import com.app.todos.application.usecases.users.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserGateway userGateway;
    @Mock
    private PasswordUseCase encoder;
    @Mock
    private TokenGateway tokenGateway;
    @InjectMocks
    private UserUseCase userUseCase;

    User user = new User(BigInteger.valueOf(1), "Brian", "brian@gmail.com", "12345");
    UserRequestDTO userRequestDTO = new UserRequestDTO("Brian", "brian@gmail.com", "12345");
    UserUpdateDTO userUpdateDTO = new UserUpdateDTO("New Brian", "new email", "12345", "new pass");
    String token = "token";

    @Test
    void getUser() {
        when(userGateway.get(any(BigInteger.class))).thenReturn(user);
        when(tokenGateway.validate(token)).thenReturn(user.getEmail());

        User getFunction = userUseCase.get(BigInteger.valueOf(2000), token);

        assertEquals(getFunction, user);
        assertEquals(getFunction.getEmail(), user.getEmail());
        assertDoesNotThrow(() -> getFunction);

        verify(userGateway, times(1)).get(any(BigInteger.class));
        verify(tokenGateway, times(1)).validate(any(String.class));
    }

    @Test
    void newUserTest() {
        when(userGateway.findByEmail(any(String.class))).thenReturn(null);
        when(userGateway.create(any(User.class))).thenReturn(user);

        assertEquals(user.getEmail(), userUseCase.create(user).getEmail());
        assertDoesNotThrow(() -> {
            user.setPassword("1234");
            userUseCase.create(user);
        });

        verify(userGateway, times(2)).create(any(User.class));
        verify(userGateway, times(2)).findByEmail(any(String.class));
    }

    @Test
    void update() {
        when(userGateway.get(any(BigInteger.class))).thenReturn(user);
        when(tokenGateway.validate(token)).thenReturn(user.getEmail());
        when(encoder.matches(userUpdateDTO.currentPassword(), user.getPassword())).thenReturn(true);
        when(userGateway.update(any(BigInteger.class), any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> {
            userUseCase.update(BigInteger.valueOf(2500), user, "12345", token);
        });

        verify(userGateway, times(1)).get(any(BigInteger.class));
        verify(tokenGateway, times(1)).validate(any(String.class));
        verify(userGateway, times(1)).update(any(BigInteger.class), any(User.class));
    }

    @Test
    void deleteUser() {
        when(userGateway.get(any(BigInteger.class))).thenReturn(user);
        when(tokenGateway.validate(token)).thenReturn(user.getEmail());

        assertEquals(user, userUseCase.delete(BigInteger.valueOf(1005), token));
        assertEquals(
            user.getEmail(),
            userUseCase.delete(BigInteger.valueOf(1005), token)
                .getEmail()
        );
        assertDoesNotThrow(() -> {
            userUseCase.delete(BigInteger.valueOf(1005), token);
        });

        verify(userGateway, times(3)).get(any(BigInteger.class));
        verify(tokenGateway, times(3)).validate(any(String.class));
        verify(userGateway, times(3)).delete(any(BigInteger.class));
    }
}
