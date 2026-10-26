package com.matheusgondra.guardadin.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matheusgondra.guardadin.auth.dto.UserDetails;
import com.matheusgondra.guardadin.auth.usecase.SignupParam;
import com.matheusgondra.guardadin.cryptography.service.HashService;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.exception.UserAlreadyExistsException;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {
    private final SignupParam signupParam =
            new SignupParam("anyFirstName", "anyLastName", "any.email@example.com", "anyPassword@123");
    private final UUID userIdMock = UUID.randomUUID();
    private final LocalDateTime fixedNow = LocalDateTime.of(2026, 1, 1, 0, 0);

    @InjectMocks
    private SignupService sut;

    @Mock
    private UserRepository repository;

    @Mock
    private HashService hashService;

    @BeforeEach
    void setup() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        lenient().when(hashService.hash(anyString())).thenReturn("anyHash");

        User userMock = User.builder()
                .id(userIdMock)
                .firstName(signupParam.firstName())
                .lastName(signupParam.lastName())
                .email(signupParam.email())
                .password(signupParam.password())
                .createdAt(fixedNow)
                .updatedAt(fixedNow)
                .build();

        lenient().when(repository.save(any(User.class))).thenReturn(userMock);
    }

    @Test
    void shouldCallFindByEmailMethod() {
        sut.execute(signupParam);

        verify(repository, times(1)).findByEmail(signupParam.email());
    }

    @Test
    void shouldThrowExceptionIfUserRepositoryReturnAUser() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> sut.execute(signupParam));
    }

    @Test
    void shouldCallHashService() {
        sut.execute(signupParam);

        verify(hashService, times(1)).hash(signupParam.password());
    }

    @Test
    void shouldCallSaveMethod() {
        sut.execute(signupParam);

        verify(repository).save(any(User.class));
    }

    @Test
    void shouldReturnRegisterUserResponseOnSuccess() {
        UserDetails response = sut.execute(signupParam);

        UserDetails expected =
                new UserDetails(userIdMock, "anyFirstName", "anyLastName", "any.email@example.com", fixedNow, fixedNow);

        assertEquals(expected, response);
    }
}
