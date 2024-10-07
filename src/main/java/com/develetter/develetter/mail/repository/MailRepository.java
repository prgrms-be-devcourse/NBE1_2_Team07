package com.develetter.develetter.mail.repository;

import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {

    // userId를 기준으로 filtered_job_posting과 filtered_blog 테이블을 JOIN 하여 필요한 데이터 가져오는 쿼리
//    @Query("SELECT u.id, jp.id, bl.id FROM User u " +
//            "LEFT JOIN FilteredJobPosting jp ON u.id = jp.userId " +
//            "LEFT JOIN FilteredBlogPosting bl ON u.id = bl.userId")
//    List<Object[]> findAllUserWithFilteredData();

    @Query("SELECT m FROM Mail m WHERE m.deleted = false AND m.sendingCheck = false")
    Optional<List<Mail>> findBySendingCheckIsFalse();

    List<Mail> findByDeletedIsFalse();
}

