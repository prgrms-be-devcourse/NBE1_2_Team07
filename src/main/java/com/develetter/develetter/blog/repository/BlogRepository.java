package com.develetter.develetter.blog.repository;

import com.develetter.develetter.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // 링크가 이미 존재하는지 확인
    boolean existsByLink(String link);
}
