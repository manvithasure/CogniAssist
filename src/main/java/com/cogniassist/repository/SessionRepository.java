package com.cogniassist.repository;

import com.cogniassist.model.SessionData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionRepository extends MongoRepository<SessionData, String> {
    List<SessionData> findByFatigueLevel(String fatigueLevel);
}