package com.develetter.develetter.blog.controller;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.service.InterestServiceImpl;
import com.develetter.develetter.jobposting.UserFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //타임리프 테스트 위해서 컨트롤러로 해둠
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/interest")
public class InterestController {

    private final InterestServiceImpl interestServiceImpl;
    private final UserFilterRepository userFilterRepository;

    //interest 페이지 렌더링
    @GetMapping
    public String showInterestPage() {
        return "interest";  // interest.html 템플릿 반환
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String searchQuery, @RequestParam Long userId, Model model) {
        // UserFilter를 userId로 찾음
        userFilterRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // UserId와 searchQuery를 기반으로 블로그를 가져옴
        Blog blog = interestServiceImpl.getRandomBlogBySearchQuery(userId, searchQuery);


        if (blog != null) {
            model.addAttribute("blog", blog);
            log.info("블로그 글이 성공적으로 이메일로 전송되었습니다.");
        } else {
            model.addAttribute("errorMessage", "해당 관심사에 맞는 블로그 글이 없습니다.");
            log.warn("블로그 글을 찾을 수 없습니다.");
        }

        return "interest";
    }
}


