package com.matheusgondra.guardadin.auth.service;

import com.matheusgondra.guardadin.auth.dto.UserDetails;
import com.matheusgondra.guardadin.auth.usecase.SignupParam;
import com.matheusgondra.guardadin.auth.usecase.SignupUseCase;
import com.matheusgondra.guardadin.cryptography.service.HashService;
import com.matheusgondra.guardadin.user.entity.User;
import com.matheusgondra.guardadin.user.exception.UserAlreadyExistsException;
import com.matheusgondra.guardadin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignupService implements SignupUseCase {
    private final UserRepository repository;
    private final HashService hashService;

    @Transactional
    @Override
    public UserDetails execute(SignupParam param) {
        repository.findByEmail(param.email()).ifPresent(u -> {
            throw new UserAlreadyExistsException();
        });

        var user = User.builder()
                .firstName(param.firstName())
                .lastName(param.lastName())
                .email(param.email())
                .password(hashService.hash(param.password()))
                .build();
        var userCreated = repository.save(user);

        return new UserDetails(
                userCreated.getId(),
                userCreated.getFirstName(),
                userCreated.getLastName(),
                userCreated.getEmail(),
                userCreated.getCreatedAt(),
                userCreated.getUpdatedAt()
        );
    }
}
