package com.gachon.ReAction_bank_server.dto.user.controller;

import com.gachon.ReAction_bank_server.dto.user.service.RegisterServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "로그인에 사용할 ID를 입력해주세요!")
    private String userId;

    @NotBlank(message = "로그인에 사용할 비밀번호를 입력해주세요!")
    private String pw;

    @NotBlank(message = "이름은 필수입니다!")
    private String name;

    /**
     * 계좌번호는 111-222-3333 같은 양식이여야 함.
     * ^ : start, $ : end
     */
    @NotBlank(message = "계좌번호는 필수입니다!")
    @Pattern(regexp="^\\d{3}-\\d{3}-\\d{4}$", message = "계좌번호 양식에 맞춰 입력해주세요!")
    private String accountNum;

    @Builder
    private RegisterRequest(String userId, String pw, String name, String accountNum) {
        this.userId = userId;
        this.pw = pw;
        this.name = name;
        this.accountNum = accountNum;
    }

    public static RegisterRequest of(String userId, String pw, String name, String accountNum){
        return RegisterRequest.builder()
                .userId(userId)
                .pw(pw)
                .name(name)
                .accountNum(accountNum)
                .build();
    }

    public RegisterServiceRequest to(){
        return RegisterServiceRequest.builder()
                .userId(this.userId)
                .pw(this.pw)
                .name(this.name)
                .accountNum(this.accountNum)
                .build();
    }
}
