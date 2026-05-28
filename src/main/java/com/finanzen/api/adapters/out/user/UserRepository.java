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

/**
 * Persistence Adapter (Outbound Adapter) for User infrastructure management.
 * <p>
 * This component acts as a bridge between the core application port ({@link UserRepositoryPort})
 * and Spring Data JPA. It translates domain models to entities, handles persistence, and unpacks
 * framework pagination classes back into neutral structures.
 * </p>
 */
@Component
public class UserRepository implements UserRepositoryPort {

    private final JpaUserRepository repository;

    public UserRepository(JpaUserRepository repository) {
        this.repository = repository;
    }

    /**
     * Maps a pure domain user model to a database entity, persists it, and maps it back.
     *
     * @param user the pure domain entity to save.
     * @return the persisted domain object with its database-assigned state.
     */
    @Override
    public User save(User user) {
        JpaUserEntity savEntity = repository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(savEntity);
    }

    /**
     * Retrieves a database record by its primary key and transforms it into a domain Optional.
     *
     * @param id the unique primary key identifier.
     * @return an Optional containing the mapped pure {@link User}, or empty.
     */
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(UserMapper::toDomain);
    }

    /**
     * ADMIN ONLY: Retrieves a paginated list of all system users, translating Spring Data
     * {@link Page} structures into framework-agnostic {@link PageResult} containers.
     *
     * @param page zero-based page index.
     * @param size maximum number of elements per page.
     * @return a decoupled {@link PageResult} containing system users.
     */
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

    /**
     * Checks infrastructure data storage to see if an email address is already taken.
     *
     * @param email the email identity string to check.
     * @return true if the email is found in storage, false otherwise.
     */
    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    /**
     * Looks up a user record by their email address and safely maps the entity to a pure domain model.
     *
     * @param email the target email.
     * @return an Optional containing the mapped pure {@link User}, or empty.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toDomain);
    }
}