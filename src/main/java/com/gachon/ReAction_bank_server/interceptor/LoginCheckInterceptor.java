package com.gachon.ReAction_bank_server.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gachon.ReAction_bank_server.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // 세션 없이 접근했을 경우 401 반환
        if (session == null || session.getAttribute("loginUser") == null){
            ObjectMapper objectMapper = new ObjectMapper();

            // response 헤더 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            // 빈 메시지 대신, ApiResponse 이용해 메시지 생성
            ApiResponse unauthorizedMessage = ApiResponse.fail(HttpStatus.UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(unauthorizedMessage));

            return false;
        }
        return true;
    }
}
