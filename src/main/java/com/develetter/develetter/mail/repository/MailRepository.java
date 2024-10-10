package com.develetter.develetter.mail.repository;

import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {

    @Query("SELECT m FROM Mail m WHERE m.deleted = false AND m.sendingCheck = false")
    Optional<List<Mail>> findBySendingCheckIsFalse();

    List<Mail> findByDeletedIsFalse();
}

