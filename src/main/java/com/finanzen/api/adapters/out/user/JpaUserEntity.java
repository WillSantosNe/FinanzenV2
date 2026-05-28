package com.finanzen.api.adapters.out.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.finanzen.api.domain.user.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Infrastructure Database Entity representing the 'users' table in the database.
 * <p>
 * This class implements Spring Security's {@link UserDetails} interface. By hosting
 * framework-specific security contracts exclusively inside this database entity, we prevent
 * Spring Security dependencies from leaking into the core business domain layer.
 * </p>
 */
@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaUserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Converts the user's role into a Spring Security granted authority.
     *
     * @return a collection containing the mapped {@link SimpleGrantedAuthority} prefixed with 'ROLE_'.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    /**
     * Returns the password used to authenticate the user.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the username used to authenticate the user (the email in this system).
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Indicates whether the user's account has expired.
     * @return true (accounts do not expire in this system).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * @return true (accounts are unlocked by default).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * @return true (credentials do not expire automatically).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * @return true (users are enabled upon registration).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}