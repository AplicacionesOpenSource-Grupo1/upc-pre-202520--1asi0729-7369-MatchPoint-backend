package com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByEmail(String email);
    boolean existsByEmail(String email);
}

