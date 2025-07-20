package com.doitmoney.backend.account.controller;

import java.util.List;
import com.doitmoney.backend.account.entity.Account;
import com.doitmoney.backend.account.service.AccountService;
import com.doitmoney.backend.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService svc;

    public AccountController(AccountService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Account> list(Authentication auth) {
        Long uid = ((CustomUserDetails)auth.getPrincipal()).getUserId();
        return svc.getAccountsByUserId(uid);
    }

    @PostMapping
    public Account create(Authentication auth,
                          @RequestBody Account acct) {
        Long uid = ((CustomUserDetails)auth.getPrincipal()).getUserId();
        return svc.addAccount(uid, acct);
    }

    @PutMapping("/{accountId}")
    public Account update(Authentication auth,
                          @PathVariable Long accountId,
                          @RequestBody Account upd) {
        Long uid = ((CustomUserDetails)auth.getPrincipal()).getUserId();
        return svc.updateAccount(uid, accountId, upd);
    }

    @DeleteMapping("/{accountId}")
    public void delete(Authentication auth,
                       @PathVariable Long accountId) {
        Long uid = ((CustomUserDetails)auth.getPrincipal()).getUserId();
        svc.deleteAccount(uid, accountId);
    }
}