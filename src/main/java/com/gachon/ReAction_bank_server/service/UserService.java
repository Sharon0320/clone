package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.dto.user.response.LoginResponse;
import com.gachon.ReAction_bank_server.dto.user.response.RegisterResponse;
import com.gachon.ReAction_bank_server.dto.user.service.LoginServiceRequest;
import com.gachon.ReAction_bank_server.dto.user.service.RegisterServiceRequest;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    /**
     * 회원가입
     *    - 중복된 ID, accountNum을 입력했는지 검사한 뒤 (IllegalArgumentException 발생),
     *      새로운 User와 Account DB에 저장
     * @param req (userId, pw, name, accountNum)
     * @return res
     */
    public RegisterResponse register(RegisterServiceRequest req) {

        // 1. 중복된 ID, 계좌번호가 있는지 확인
       if(userRepository.existsByUserId(req.getUserId()))
           throw new IllegalArgumentException("다른 사람이 같은 ID를 사용중입니다! 다른 ID를 입력해주세요!");

       if(accountRepository.existsByAccountNum(req.getAccountNum()))
           throw new IllegalArgumentException("다른 사람이 같은 계좌번호를 사용중입니다! 다른 계좌번호를 입력해주세요!");

       // 2. req 정보 이용해 새로운 User, Account 생성
        User user = User.of(req.getName(), req.getUserId(), req.getPw());
        User savedUser = userRepository.save(user);

        Account account = Account.create(req.getAccountNum(), savedUser);
        Account savedAccount = accountRepository.save(account);

        return RegisterResponse.of(savedUser, savedAccount);
    }

    public User login(LoginServiceRequest req){

        // 1. ID, PW 가진 User 가져옴
        return userRepository
                .findByuserId(req.getUserId())
                .filter(m -> m.getPw().equals(req.getPw()))
                .orElseThrow(() -> new IllegalArgumentException("ID 또는 비밀번호가 잘못되었습니다!"));
//        return new LoginResponse(user.getName(), user.getUserId(), account.getAccountNum());
    }
}
