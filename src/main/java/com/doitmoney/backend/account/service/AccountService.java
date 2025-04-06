package com.doitmoney.backend.account.service;

import com.doitmoney.backend.account.entity.Account;
import com.doitmoney.backend.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository) {
         this.accountRepository = accountRepository;
    }
    
    public Account addAccount(Account account) {
         return accountRepository.save(account);
    }
    
    public List<Account> getAccountsByUserId(Long userId) {
         return accountRepository.findByUserId(userId);
    }
}