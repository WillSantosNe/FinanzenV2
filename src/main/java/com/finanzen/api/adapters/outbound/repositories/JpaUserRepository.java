package com.finanzen.api.adapters.outbound.repositories;

import java.util.Optional;

import com.finanzen.api.adapters.outbound.entities.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the User entity.
 * <p>
 * This interface handles the database operations for users. It is utilized by 
 * the security layer to retrieve user credentials during the authentication process.
 * </p>
 */
@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {

    /**
     * Retrieves a user entity based on their email address.
     *
     * @param email the user's email address (used as the username in this system).
     * @return an {@link Optional} containing the user if found.
     */
    Optional<JpaUserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
