package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterestServicelmpl {

    private final BlogRepository blogRepository;
    // 관심사 목록을 반환하는 메서드
    public List<String> getInterests() {
        return List.of("Java", "AI", "Spring");  // 관심사 목록 (필요 시 더 추가 가능)
    }

    // 사용자가 선택한 관심사 기반으로 랜덤 블로그 글을 반환
    public Blog getRandomBlogBySearchQuery(String searchQuery) {
        List<Blog> blogs = blogRepository.findBlogsBySearchQuery(searchQuery);

        if (blogs.isEmpty()) {
            log.info("해당 관심사에 맞는 블로그 글이 없습니다.");
        }

        // 여러 결과 중 하나를 랜덤으로 선택
        Blog randomBlog = blogs.get(new Random().nextInt(blogs.size()));
        log.info("블로그 글이 성공적으로 반환되었습니다.");
        return randomBlog;
    }
}
