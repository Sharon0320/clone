package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.dto.ApiResponse;
import com.gachon.ReAction_bank_server.dto.account.response.UserAccountResponse;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * If session is none, it will be caught by Interceptor! redirect to /login!
     * @param loginUser
     * @return
     */
    @GetMapping("/own")
    public ApiResponse<UserAccountResponse> getUserAccount(@SessionAttribute(name = "loginUser") User loginUser){
        return ApiResponse.success(accountService.getUserAccount(loginUser));
    }
}
