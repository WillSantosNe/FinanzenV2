package com.finanzen.api.infrastructure.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finanzen.api.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * <p>
 * This service acts as the bridge between the application's database and the 
 * Spring Security authentication manager. It fetches the {@link UserEntity} 
 * (which implements UserDetails) using the provided email address.
 * </p>
 */
@Service
@RequiredArgsConstructor // Para não precisar criar o construtor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    /**
     * Locates the user based on the email address.
     *
     * @param username the email address identifying the user whose data is required.
     * @return a fully populated user record (never null).
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
