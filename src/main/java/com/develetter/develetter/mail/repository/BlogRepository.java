package com.develetter.develetter.mail.repository;

import com.develetter.develetter.mail.dto.Blogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blogs, Long> {
}
