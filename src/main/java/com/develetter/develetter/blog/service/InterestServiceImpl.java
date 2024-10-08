package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.entity.FilteredBlog;
import com.develetter.develetter.blog.repository.BlogRepository;
import com.develetter.develetter.blog.repository.FilteredBlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterestServiceImpl implements InterestService{

    private final BlogRepository blogRepository;
    private final FilteredBlogRepository filteredBlogRepository;

    // 관심사 목록을 반환하는 메서드, 이곳에서 관심사 키워드를 관리함
    public List<String> getInterests() {
        return List.of("CSS", "Vue", "TypeScript" /*"Html","Javascript", "Next.js",
                "Swift", "Kotlin", "Java", "Spring", "Node.js", "Python"*/);  // 관심사 목록
    }

    // 사용자가 선택한 관심사 기반으로 받지 않은 랜덤 블로그 글을 반환
    public Blog getRandomBlogBySearchQuery(Long userId, String searchQuery) {
        List<Blog> blogs = blogRepository.findBlogsBySearchQuery(searchQuery);

        if (blogs.isEmpty()) {
            log.info("해당 관심사에 맞는 블로그 글이 없습니다.");
            return null;
        }

        // 사용자가 받지 않은 블로그 필터링
        List<Blog> unreceivedBlogs = blogs.stream()
                .filter(blog -> !filteredBlogRepository.existsByUserIdAndBlog(userId, blog.getId()))
                .toList();

        if (unreceivedBlogs.isEmpty()) {
            log.info("사용자가 받지 않은 블로그 글이 없습니다."); //추후 프론트에서 다른 관심사를 선택하시겠습니까? 와 같은 메시지 출력해줘도 좋을듯
            return null;
        }

        // 여러 결과 중 하나를 랜덤으로 선택
        Blog randomBlog = unreceivedBlogs.get(new Random().nextInt(unreceivedBlogs.size()));

        saveFilteredBlog(userId, randomBlog.getId());

        return randomBlog;
    }

    private void saveFilteredBlog(Long userId, Long blogId) {
        FilteredBlog filteredBlog = FilteredBlog.builder()
                .userId(userId)
                .blog(blogId)
                .build();
        filteredBlogRepository.save(filteredBlog);
        log.info("FilteredBlog 저장 완료: user={}, blog={}", userId, blogId);
    }
}
