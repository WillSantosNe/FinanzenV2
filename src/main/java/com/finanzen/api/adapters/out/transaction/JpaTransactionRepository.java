package com.finanzen.api.adapters.out.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTransactionRepository extends JpaRepository<JpaTransactionEntity, Long> {
    Page<JpaTransactionEntity> findAllByUserEmail(String userEmail, Pageable pageable);
}
