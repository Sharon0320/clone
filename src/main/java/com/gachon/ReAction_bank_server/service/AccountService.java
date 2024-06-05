package com.gachon.ReAction_bank_server.service;

import com.gachon.ReAction_bank_server.dto.account.response.TransferResponse;
import com.gachon.ReAction_bank_server.dto.account.response.UserAccountResponse;
import com.gachon.ReAction_bank_server.dto.account.service.TransferServiceRequest;
import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.Statement;
import com.gachon.ReAction_bank_server.entity.User;
import com.gachon.ReAction_bank_server.repository.AccountRepository;
import com.gachon.ReAction_bank_server.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gachon.ReAction_bank_server.entity.transactionType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final StatementRepository statementRepository;

    public UserAccountResponse getUserAccount(User loginUser) {
        Account account = accountRepository
                .findByUser(loginUser)
                .orElseThrow(() -> new IllegalArgumentException("소유 중인 계좌가 없습니다!"));

        return UserAccountResponse.of(account.getId(), account.getAccountNum(), account.getBalance());
    }

    @Transactional
    public UserAccountResponse deposit(Long accountId, int amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다!"));
        account.deposit(amount);
        accountRepository.save(account);

        Statement transferStatement = Statement.of(account, account, amount, DEPOSIT);
        statementRepository.save(transferStatement);

        return UserAccountResponse.of(account.getId(), account.getAccountNum(), account.getBalance());
    }

    @Transactional
    public UserAccountResponse withdraw(Long accountId, int amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다!"));
        account.withdraw(amount);
        accountRepository.save(account);
        return UserAccountResponse.of(account.getId(), account.getAccountNum(), account.getBalance());
    }

    /**
     * 이체
     * loginUser 계좌에서 req에 담긴 계좌번호로 이체
     * @param loginUser, req (이체할 금액 저장)
     * @Exception 아래 조건 만족하지 못할 시 IllegalArgumentEx 반환
     * @return TransferResponse (로그인 유저 계좌의 이체 후 잔액 반환)
     */
    @Transactional
    public TransferResponse transfer(User loginUser, TransferServiceRequest req){

        // 1. 계좌 조회
        Account loginUserAccount = accountRepository
                .findByUser(loginUser)
                .orElseThrow(() -> new IllegalArgumentException("소유 중인 계좌가 없습니다!"));

        Account receiverAccount = accountRepository
                .findDepositAccountWithLock(req.getReceiverAccountNum())
                .orElseThrow(() -> new IllegalArgumentException("송금하려는 계좌가 존재하지 않습니다!"));

        // 2. 정책 관련 validation 실행
        if(loginUserAccount.getAccountNum().equals(req.getReceiverAccountNum()))
            throw new IllegalArgumentException("본인 계좌로는 이체할 수 없습니다!");

        if(req.getAmount() <= 0)
            throw new IllegalArgumentException("0원 이하는 송금할 수 없습니다!");

        if (req.getAmount() > loginUserAccount.getBalance())
            throw new IllegalArgumentException("계좌 잔액보다 더 송금할 수 없습니다!");

        if(req.getAmount() % 10 != 0)
            throw new IllegalArgumentException("일 원 단위의 돈은 송금할 수 없습니다!");

        // 3. 이체
//        accountRepository.withdraw(loginUserAccount, req.getAmount());
//        accountRepository.deposit(receiverAccount, req.getAmount());
        int loginUserBalance = loginUserAccount.transfer(receiverAccount, req.getAmount());

        // 4. 이체 기록 Statement Table에 기록 (잔액이 아닌, 이체한 금액이 기록)
        Statement transferStatement = Statement.of(loginUserAccount, receiverAccount, req.getAmount(), TRANSFER);
        statementRepository.save(transferStatement);

        return TransferResponse.of(loginUserAccount.getBalance());
    }
}
