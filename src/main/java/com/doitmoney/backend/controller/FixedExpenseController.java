package com.doitmoney.backend.controller;

import com.doitmoney.backend.entity.FixedExpense;
import com.doitmoney.backend.service.FixedExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fixed-expenses")
public class FixedExpenseController {

    private final FixedExpenseService fixedExpenseService;

    @Autowired
    public FixedExpenseController(FixedExpenseService fixedExpenseService) {
        this.fixedExpenseService = fixedExpenseService;
    }

    // 고정지출 등록 (userId 경로 변수 사용 - 실제로는 사용자 인증 후 처리하는 것이 좋습니다)
    @PostMapping("/{userId}")
    public FixedExpense addFixedExpense(@PathVariable Long userId, @RequestBody FixedExpense fixedExpense) {
        // 클라이언트에서 user 정보가 전달되지 않는 경우, 별도 User 조회 후 설정 필요
        // 여기서는 단순화를 위해 fixedExpense 객체에 user 정보가 세팅되어 있다고 가정합니다.
        return fixedExpenseService.addFixedExpense(fixedExpense);
    }

    // 특정 유저의 고정지출 조회
    @GetMapping("/{userId}")
    public List<FixedExpense> getFixedExpensesByUserId(@PathVariable Long userId) {
        return fixedExpenseService.getFixedExpensesByUserId(userId);
    }
}
