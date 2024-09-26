package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.repository.BlogRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SearchService {

    private static final String DEFAULT_IMAGE_URL = "https://img.freepik.com/premium-vector/crescent-moon-shining-starry-night-sky_1334819-5377.jpg";

    private final String API_KEY = "AIzaSyCiVrpok4THaOh_jelRdz0ILGeQHlPf5pg";  // Google Cloud에서 생성한 API 키
    private final String SEARCH_ENGINE_ID = "07f528fe836de4707";  // Programmable Search Engine ID
    //최근 두달이내 작성한 글만 불러오게 설정
    private final String GOOGLE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID + "&dateRestrict=d60&q=";


    private final BlogRepository blogRepository;

    public SearchService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    //이 메서드 한번 호출시마다 검색엔진 할당량 1감소 (하루 100이므로 조심)
    public void searchAndSaveBlogPosts(String query) {
        RestTemplate restTemplate = new RestTemplate();
        int startIndex = 1;  // 검색 결과의 시작 인덱스 (페이징 처리 위해서 사용)
        int savedCount = 0;  // 저장된 블로그 글 개수
        int requiredCount = 10;  // 최소한 저장해야 할 세부 글 개수, 추후 한 번에 10개보다 데이터를 많이 받아오고싶다면 변경해도됨
        boolean hasMoreResults = true;

        try {
            // 세부 글이 10개 저장될 때까지 반복
            while (hasMoreResults && savedCount < requiredCount) {
                String url = GOOGLE_SEARCH_URL + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()) + "&num=10&start=" + startIndex;
                String response = restTemplate.getForObject(url, String.class);

                // JSON 응답을 받아서 처리
                JSONObject jsonResponse = new JSONObject(response);
                // 검색 결과가 없으면 프로그램 종료
                if (!jsonResponse.has("items")) {
                    System.out.println("검색결과가 없습니다.");
                    return;
                }
                JSONArray items = jsonResponse.getJSONArray("items");

                System.out.println("Number of items returned: " + items.length());

                if (items.length() == 0) {
                    hasMoreResults = false;  // 더 이상 결과가 없으면 중단
                }

                // 응답 데이터 반복 처리
                for (int i = 0; i < items.length() && savedCount < requiredCount; i++) {
                    JSONObject item = items.getJSONObject(i);

                    // 기본 데이터 추출
                    String title = item.getString("title");
                    String snippet = item.getString("snippet");
                    String link = item.getString("link");

                    // 세부 블로그 글인지 확인
                    if (isBlogDetailPage(link)) {
                        // 중복된 링크인지 확인
                        if (!blogRepository.existsByLink(link)) {
                            // pagemap에서 추가 정보를 추출하고 데이터 저장
                            processPagemap(item, title, snippet, link);
                            savedCount++;  // 저장된 블로그 글 수 증가
                        } else {
                            System.out.println("중복된 링크이므로 저장하지 않음: " + link);
                        }
                    }
                }

                // 다음 페이지로 이동
                startIndex += 10;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processPagemap(JSONObject item, String title, String snippet, String link) {
        String imageUrl = null;

        try {
            if (item.has("pagemap")) {
                JSONObject pagemap = item.getJSONObject("pagemap");

                // 1. cse_image로부터 이미지 URL 추출
                if (pagemap.has("cse_image")) {
                    JSONArray cseImageArray = pagemap.getJSONArray("cse_image");
                    imageUrl = cseImageArray.getJSONObject(0).getString("src");
                    System.out.println("cse_image에서 추출한 Image URL: " + imageUrl);
                }

                // 2. metatags로부터 Open Graph 데이터 추출 (og:title, og:image 등)
                if (pagemap.has("metatags")) {
                    JSONObject metatags = pagemap.getJSONArray("metatags").getJSONObject(0);

                    // Open Graph 제목이 있으면 덮어쓰기
                    if (metatags.has("og:title")) {
                        title = metatags.getString("og:title");
                        System.out.println("OG Title: " + title);
                    }

                    // Open Graph 설명이 있으면 덮어쓰기
                    if (metatags.has("og:description")) {
                        snippet = metatags.getString("og:description");
                        System.out.println("OG Description: " + snippet);
                    }

                    // Open Graph 이미지 URL이 있으면 덮어쓰기
                    if (metatags.has("og:image")) {
                        imageUrl = metatags.getString("og:image");
                        System.out.println("OG Image URL: " + imageUrl);
                    }
                }
            }

            // 3단계로 넘어가서 데이터를 저장
            saveBlogData(title, snippet, link, imageUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBlogData(String title, String snippet, String link, String imageUrl) {
        try {
            // 이미지 URL이 없으면 기본 이미지 사용
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = DEFAULT_IMAGE_URL;
            }

            // Blog 객체 생성
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setSnippet(snippet);
            blog.setLink(link);
            blog.setImageUrl(imageUrl);

            // 데이터베이스에 저장
            blogRepository.save(blog);
            System.out.println("Blog 저장됨: " + title);

        } catch (Exception e) {
            System.out.println("Blog 저장 실패 " + title);
            e.printStackTrace();
        }
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    private boolean isBlogDetailPage(String link) {
        // Toss 블로그의 경우: /article/가 포함된 링크만 세부 글로 인정, /design, /tech 등은 목록 페이지로 처리
        if (link.contains("toss.tech")) {
            if (link.contains("/article/")) {
                return true;
            } else if (link.contains("/design") || link.contains("/tech")) {
                System.out.println("Toss 블로그 카테고리 페이지는 저장하지 않음: " + link);
                return false;
            } else {
                System.out.println("Toss 블로그 목록 페이지는 저장하지 않음: " + link);
                return false;
            }
        }

        // SK플래닛 블로그의 경우: 루트 경로('/')는 목록 페이지로 간주하고, 그 외는 세부 글로 간주
        if (link.contains("techtopic.skplanet.com")) {
            if (link.equals("https://techtopic.skplanet.com/")) {
                System.out.println("SK플래닛 블로그 루트 목록 페이지는 저장하지 않음: " + link);
                return false;  // 루트 경로만 목록 페이지로 간주
            } else {
                return true;  // 다른 경로는 세부 글로 간주
            }
        }

        // Devsisters 블로그의 경우: /posts/가 포함된 링크만 세부 글로 인정
        if (link.contains("tech.devsisters.com") && !link.contains("/posts/")) {
            System.out.println("Devsisters 블로그 목록 페이지는 저장하지 않음: " + link);
            return false;
        }

        // DevOcean 블로그의 경우: techBoardDetail.do가 포함된 링크만 세부 글로 인정
        if (link.contains("devocean.sk.com") && !link.contains("techBoardDetail.do")) {
            System.out.println("DevOcean 블로그 목록 페이지는 저장하지 않음: " + link);
            return false;
        }

        // KakaoPay 블로그의 경우: /post/가 포함된 링크만 세부 글로 인정
        if (link.contains("tech.kakaopay.com") && !link.contains("/post/")) {
            System.out.println("KakaoPay 블로그 목록 페이지는 저장하지 않음: " + link);
            return false;
        }
        // 그 외의 경우 세부 글로 인정
        return true;
    }

}

