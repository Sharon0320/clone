package com.gachon.ReAction_bank_server.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("로그인 체크 인터셉터 실행");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null){
            log.info("미인증 사용자 요청");
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
