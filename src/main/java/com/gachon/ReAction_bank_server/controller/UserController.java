package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.dto.ApiResponse;
import com.gachon.ReAction_bank_server.dto.user.controller.RegisterRequest;
import com.gachon.ReAction_bank_server.dto.user.RegisterResponse;
import com.gachon.ReAction_bank_server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest req){
        return ApiResponse.success(userService.register(req.to()));
    }
}
