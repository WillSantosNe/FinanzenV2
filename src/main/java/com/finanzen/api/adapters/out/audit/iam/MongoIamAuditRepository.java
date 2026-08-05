package com.finanzen.api.adapters.out.audit.iam;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoIamAuditRepository extends MongoRepository<IamAuditEntity, String> {
}
