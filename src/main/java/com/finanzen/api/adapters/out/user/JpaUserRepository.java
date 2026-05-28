package com.finanzen.api.adapters.out.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Infrastructure Repository for {@link JpaUserEntity}.
 * <p>
 * Handles raw database operations for users. This interface should remain encapsulated
 * within the outbound persistence adapter layer and never leak into application use cases.
 * </p>
 */
@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {

    /**
     * Custom database query to find a user entity based on their unique email address.
     *
     * @param email the user's email address.
     * @return an {@link Optional} containing the found database entity, or empty.
     */
    Optional<JpaUserEntity> findByEmail(String email);

    /**
     * Checks database constraints to see if an email is already registered.
     *
     * @param email the target email to check.
     * @return true if the email exists in the database, false otherwise.
     */
    boolean existsByEmail(String email);
}