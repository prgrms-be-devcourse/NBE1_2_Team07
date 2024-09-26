package com.develetter.develetter.mail.repository;

import com.develetter.develetter.mail.dto.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<Users, Long> {
}
