package com.develetter.develetter.blog.repository;

import com.develetter.develetter.blog.entity.FilteredBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilteredBlogRepository extends JpaRepository<FilteredBlog, Long> {
    boolean existsByUserIdAndBlog(Long userId, Long blog); // UserFilter 기반 블로그 중복 확인

    // 특정 userId에 해당하는 가장 최근의 FilteredBlog를 반환
    Optional<FilteredBlog> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
