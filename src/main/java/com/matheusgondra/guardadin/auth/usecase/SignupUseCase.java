package com.matheusgondra.guardadin.auth.usecase;

import com.matheusgondra.guardadin.auth.dto.UserDetails;

public interface SignupUseCase {
    UserDetails execute(SignupParam param);
}
