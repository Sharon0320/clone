package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.dto.ApiResponse;
import com.gachon.ReAction_bank_server.dto.user.controller.LoginRequest;
import com.gachon.ReAction_bank_server.dto.user.controller.RegisterRequest;
import com.gachon.ReAction_bank_server.dto.user.response.RegisterResponse;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest req){
        return ApiResponse.success(userService.register(req.to()));
    }

    @PostMapping("/login")
    public ApiResponse login(@SessionAttribute(required = false, name = "loginUser") User loginUser,
                             @Valid @RequestBody LoginRequest req,
                             HttpServletRequest servletRequest){

        if (loginUser != null) return ApiResponse.success();

        User user = userService.login(req.to());
        HttpSession session = servletRequest.getSession();
        session.setAttribute("loginUser", user);
        return ApiResponse.success();
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest servletRequest){
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.success();
    }
}
