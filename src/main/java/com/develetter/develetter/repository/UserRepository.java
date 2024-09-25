package com.develetter.develetter.repository;

import com.develetter.develetter.user.UserEntity;
import org.apache.el.parser.BooleanNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserId(String userId);
    UserEntity findByUserId(String userId);
}
