package com.develetter.develetter.blog.Util;

public class BlogUtil {

    // 블로그 링크가 세부 페이지인지 확인하는 메서드
    public static boolean isBlogDetailPage(String link) {
        if (link.contains("toss.tech")) {
            return link.contains("/article/");
        }

        if (link.contains("techtopic.skplanet.com")) {
            return !link.equals("https://techtopic.skplanet.com/");
        }

        if (link.contains("tech.devsisters.com")) {
            return link.contains("/posts/");
        }

        if (link.contains("devocean.sk.com")) {
            return link.contains("techBoardDetail.do");
        }

        if (link.contains("tech.kakaopay.com")) {
            return link.contains("/post/");
        }

        if (link.contains("d2.naver.com")) {
            if (link.matches("https://d2\\.naver\\.com/(helloworld|news)(\\?.*)?$")) {
                return false;
            }

            if (link.matches("https://d2\\.naver\\.com/(helloworld|news)/\\d+")) {
                return true;
            }
        }

        if (link.contains("tech.kakao.com")) {
            if (link.matches("https://tech\\.kakao\\.com/blog$")) {
                return false;
            }
            if (link.matches("https://tech\\.kakao\\.com/posts/\\d+")) {
                return true;
            }
        }

        if (link.contains("techblog.lycorp.co.jp")) {
            if (link.matches("https://techblog\\.lycorp\\.co\\.jp/ko$")) {
                return false;
            }
            if (link.matches("https://techblog\\.lycorp\\.co\\.jp/ko/.+")) {
                return true;
            }
        }

        return true;
    }
}
