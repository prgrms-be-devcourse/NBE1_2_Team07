package com.develetter.develetter.jobposting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFilterRepository extends JpaRepository<UserFilter, Long> {
    Optional<UserFilter> findByUserId(Long userId);
}
