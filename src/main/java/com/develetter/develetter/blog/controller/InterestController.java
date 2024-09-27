package com.develetter.develetter.blog.controller;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.service.InterestServicelmpl;
import com.develetter.develetter.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //타임리프 테스트 위해서 컨트롤러로 해둠
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {

    private final InterestServicelmpl interestServicelmpl;

    // GET 메서드로 /interest 페이지 렌더링
    @GetMapping
    public String showInterestPage() {
        return "interest";  // interest.html 템플릿 반환
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String searchQuery, Model model) {
        ApiResponseDto<Blog> response = interestServicelmpl.getRandomBlogBySearchQuery(searchQuery);

        if (response.getStatus() == 200) {
            model.addAttribute("blog", response.getData());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }

        return "interest";
    }
}

