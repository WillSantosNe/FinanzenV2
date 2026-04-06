package com.finanzen.api.infrastructure.repository;

import com.finanzen.api.domain.User;

/**
 * Utility class for mapping between the pure User domain model and the UserEntity.
 * <p>
 * This mapper ensures that the core domain remains isolated by translating data 
 * between the infrastructure layer and the application core.
 * </p>
 */
public class UserMapper {
    
    /**
     * Converts a JPA entity into a pure domain object.
     *
     * @param entity the JPA entity retrieved from the database.
     * @return the corresponding {@link User} domain object, or null if input is null.
     */
    public static User toDomain(UserEntity entity){
        if(entity == null){
            return null;
        }

        return new User(
            entity.getId(), 
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole());
    }

    /**
     * Converts a pure domain object into a JPA entity.
     *
     * @param domain the pure domain object.
     * @return the corresponding {@link UserEntity} ready for persistence, or null if input is null.
     */
    // Adicionado o 'static' aqui para manter o padrão!
    public static UserEntity toEntity(User domain){
        if (domain == null) {
            return null;
        }

        return new UserEntity(
            domain.getId(),
            domain.getEmail(),
            domain.getPassword(),
            domain.getRole()
        );
    }
}
