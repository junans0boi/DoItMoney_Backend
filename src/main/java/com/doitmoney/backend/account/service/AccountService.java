package com.doitmoney.backend.account.service;

import com.doitmoney.backend.account.entity.Account;
import com.doitmoney.backend.account.entity.AccountType;
import com.doitmoney.backend.account.entity.BankDetail;
import com.doitmoney.backend.account.repository.AccountRepository;
import com.doitmoney.backend.fixedExpense.repository.FixedExpenseRepository;
import com.doitmoney.backend.transaction.repository.TransactionRepository;
import com.doitmoney.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository acctRepo;
    private final UserRepository userRepo;
    private final TransactionRepository txRepo;
    private final FixedExpenseRepository fxRepo;

    public AccountService(
            AccountRepository acctRepo,
            UserRepository userRepo,
            TransactionRepository txRepo,
            FixedExpenseRepository fxRepo) {
        this.acctRepo = acctRepo;
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.fxRepo = fxRepo;
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return acctRepo.findByUserUserId(userId);
    }

    public Account addAccount(Long userId, Account acct) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        acct.setUser(user);

        // BANK 계좌가 아니면 detailType 제거
        if (acct.getAccountType() != AccountType.BANK) {
            acct.setDetailType(null);
        }
        // BANK 계좌인데 detailType이 비어있으면 기본 DEMAND로 설정
        else if (acct.getDetailType() == null) {
            acct.setDetailType(BankDetail.DEMAND);
        }

        return acctRepo.save(acct);
    }

    public Account updateAccount(Long userId, Long acctId, Account upd) {
        var ex = acctRepo.findById(acctId)
                .orElseThrow(() -> new RuntimeException("계좌 없음"));
        if (!ex.getUser().getUserId().equals(userId))
            throw new RuntimeException("본인 계좌 아닙니다");
        ex.setAccountType(upd.getAccountType());
        ex.setInstitutionName(upd.getInstitutionName());
        ex.setAccountNumber(upd.getAccountNumber());
        ex.setBalance(upd.getBalance());

        // detailType 처리
        if (upd.getAccountType() != AccountType.BANK) {
            ex.setDetailType(null);
        } else {
            ex.setDetailType(
                    upd.getDetailType() != null
                            ? upd.getDetailType()
                            : BankDetail.DEMAND);
        }

        return acctRepo.save(ex);
    }

    public void deleteAccount(Long userId, Long acctId) {
        var acct = acctRepo.findById(acctId)
                .orElseThrow(() -> new RuntimeException("계좌 없음"));
        if (!acct.getUser().getUserId().equals(userId))
            throw new RuntimeException("본인 계좌 아닙니다");

        // (cascade ALL + orphanRemoval 로 자동 삭제되지만,
        // 별도 메서드를 호출해도 무방합니다)
        fxRepo.deleteByFromAccount_Id(acctId);
        txRepo.deleteByAccount_Id(acctId);

        acctRepo.delete(acct);
    }
}