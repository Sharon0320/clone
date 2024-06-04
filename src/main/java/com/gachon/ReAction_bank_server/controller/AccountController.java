package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.dto.ApiResponse;
import com.gachon.ReAction_bank_server.dto.account.controller.AmountRequest;
import com.gachon.ReAction_bank_server.dto.account.controller.TransferRequest;
import com.gachon.ReAction_bank_server.dto.account.response.TransferResponse;
import com.gachon.ReAction_bank_server.dto.account.response.UserAccountResponse;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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


    @PostMapping("/{id}/deposit")
    public ApiResponse<UserAccountResponse> deposit(
            @SessionAttribute(name = "loginUser") User loginUser,
            @PathVariable Long id,
            @RequestBody int amount) {
        UserAccountResponse response = accountService.deposit(id, amount);
        return ApiResponse.success(response);
    }

    @PostMapping("/{id}/withdraw")
    public ApiResponse<UserAccountResponse> withdraw(
            @SessionAttribute(name = "loginUser") User loginUser,
            @PathVariable("id") Long id,
            @RequestBody AmountRequest amountResponse) {
        UserAccountResponse response = accountService.withdraw(id, amountResponse.getAmount());
        return ApiResponse.success(response);
    }

    @PostMapping("/{id}/transfer")
    public ApiResponse<TransferResponse> transfer(@SessionAttribute(name = "loginUser") User loginUser,
                                                  @PathVariable("id") Long id,
                                                  @Valid @RequestBody TransferRequest transferRequest){
        return ApiResponse.success(accountService.transfer(loginUser, transferRequest.to()));
    }
}