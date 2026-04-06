package com.finanzen.api.infrastructure.repository;

import com.finanzen.api.domain.Role;

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
 * JPA Entity representing the 'users' table in the database.
 * <p>
 * This infrastructure class is responsible for the object-relational mapping (ORM).
 * It dictates the physical constraints of the database schema and is never leaked 
 * to the core business logic.
 * </p>
 */
@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /*
    OBS: Anotacoes do persistence usamos nas entitys pois protegem o banco de dados.
    Anotacoes do validation protegem das bordas da aplicação.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Na entity para validar unicidade usamos unique e nullable para verificar se não está vazio.
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
