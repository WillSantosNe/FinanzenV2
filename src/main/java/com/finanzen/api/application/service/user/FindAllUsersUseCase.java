package com.finanzen.api.application.service.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.user.FindAllUsersPort;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class FindAllUsersUseCase implements FindAllUsersPort {

    private final UserRepositoryPort repository;

    public FindAllUsersUseCase(UserRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<User> findAll(int page, int size) {
        return repository.findAllSystemWide(page, size);
    }
}
