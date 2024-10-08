package com.develetter.develetter.blog.repository;

import com.develetter.develetter.blog.entity.FilteredBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilteredBlogRepository extends JpaRepository<FilteredBlog, Long> {
    boolean existsByUserIdAndBlog(Long userId, Long blog); // UserFilter 기반 블로그 중복 확인
}
