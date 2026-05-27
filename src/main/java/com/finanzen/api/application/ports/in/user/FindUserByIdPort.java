package com.finanzen.api.application.ports.in.user;

import com.finanzen.api.domain.user.User;

public interface FindUserByIdPort {
    User findById(Long id);
}
