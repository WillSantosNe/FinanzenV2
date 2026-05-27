package com.finanzen.api.adapters.out.user;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.out.user.UserRepositoryPort;
import com.finanzen.api.domain.user.User;
import com.finanzen.api.utils.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository implements UserRepositoryPort {

    private final JpaUserRepository repository;

    public UserRepository(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        JpaUserEntity savEntity = repository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(savEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public PageResult<User> findAllSystemWide(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JpaUserEntity> springPage = repository.findAll(pageable);

        List<User> users = springPage.getContent()
                .stream()
                .map(UserMapper::toDomain)
                .toList();

        return new PageResult<>(
                users,
                springPage.getNumber(),
                springPage.getTotalElements(),
                springPage.getTotalPages());
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toDomain);
    }
}
