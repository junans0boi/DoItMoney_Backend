package com.doitmoney.backend.fixedExpense.controller;

import java.util.List;

import com.doitmoney.backend.account.entity.Account;
import com.doitmoney.backend.account.repository.AccountRepository;
import com.doitmoney.backend.fixedExpense.dto.FixedExpenseRequest;
import com.doitmoney.backend.fixedExpense.entity.FixedExpense;
import com.doitmoney.backend.fixedExpense.service.FixedExpenseService;
import com.doitmoney.backend.user.entity.User;
import com.doitmoney.backend.security.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fixed-expenses")
public class FixedExpenseController {

    private final FixedExpenseService svc;
    private final AccountRepository accountRepo;

    public FixedExpenseController(FixedExpenseService svc,
                                  AccountRepository accountRepo) {
        this.svc = svc;
        this.accountRepo = accountRepo;
    }

    /** 전체 목록 조회 */
    @GetMapping
    public List<FixedExpense> list(@AuthenticationPrincipal CustomUserDetails user) {
        return svc.getFixedExpensesByUserId(user.getUserId());
    }

    /** 신규 등록 */
    @PostMapping
    public FixedExpense create(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestBody FixedExpenseRequest req
    ) {
        // 1) DTO → Entity
        FixedExpense fe = new FixedExpense();
        fe.setUser(new User(user.getUserId(), null, null, null, null, null));
        fe.setAmount(req.getAmount());
        fe.setCategory(req.getCategory());
        fe.setContent(req.getContent());
        fe.setDayOfMonth(req.getDayOfMonth());
        fe.setTransactionType(req.getTransactionType());
        fe.setActive(true);

        // 2) 계좌 매핑
        Account from = accountRepo.findById(req.getFromAccountId())
            .orElseThrow(() -> new RuntimeException("출금 계좌를 찾을 수 없습니다."));
        fe.setFromAccount(from);

        if (req.getToAccountId() != null) {
            Account to = accountRepo.findById(req.getToAccountId())
                .orElseThrow(() -> new RuntimeException("입금 계좌를 찾을 수 없습니다."));
            fe.setToAccount(to);
        }

        // 3) 저장
        return svc.addFixedExpense(fe);
    }

    /** 수정 */
    @PutMapping("/{id}")
    public FixedExpense update(
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long id,
        @RequestBody FixedExpenseRequest req
    ) {
        // 1) DTO → Entity
        FixedExpense fe = new FixedExpense();
        fe.setAmount(req.getAmount());
        fe.setCategory(req.getCategory());
        fe.setContent(req.getContent());
        fe.setDayOfMonth(req.getDayOfMonth());
        fe.setTransactionType(req.getTransactionType());
        fe.setActive(req.isActive());

        // 2) 계좌 매핑
        Account from = accountRepo.findById(req.getFromAccountId())
            .orElseThrow(() -> new RuntimeException("출금 계좌를 찾을 수 없습니다."));
        fe.setFromAccount(from);

        if (req.getToAccountId() != null) {
            Account to = accountRepo.findById(req.getToAccountId())
                .orElseThrow(() -> new RuntimeException("입금 계좌를 찾을 수 없습니다."));
            fe.setToAccount(to);
        }

        // 3) 업데이트
        return svc.updateFixedExpense(user.getUserId(), id, fe);
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public void delete(
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long id
    ) {
        svc.deleteFixedExpense(user.getUserId(), id);
    }
}