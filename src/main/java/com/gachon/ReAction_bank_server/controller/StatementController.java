package com.gachon.ReAction_bank_server.controller;

import com.gachon.ReAction_bank_server.dto.ApiResponse;
import com.gachon.ReAction_bank_server.dto.statement.response.StatementResponse;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@RequiredArgsConstructor
public class StatementController {
    private final StatementService statementService;

    @GetMapping("/statements")
    public ApiResponse<StatementResponse> getUserStatements(@SessionAttribute(name="loginUser") User loginUser){
        return ApiResponse.success(statementService.getUserStatements(loginUser));
    }
}
