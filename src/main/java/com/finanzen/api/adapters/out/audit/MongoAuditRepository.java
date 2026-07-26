package com.finanzen.api.adapters.out.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAuditRepository extends MongoRepository<AuditLogEntity, String> {
}
