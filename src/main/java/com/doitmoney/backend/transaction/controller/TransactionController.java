package com.doitmoney.backend.transaction.controller;

import java.io.InputStream;
import java.util.List;
import com.doitmoney.backend.transaction.entity.Transaction;
import com.doitmoney.backend.transaction.service.TransactionService;
import com.doitmoney.backend.security.CustomUserDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService svc;

    public TransactionController(TransactionService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Transaction> list(@AuthenticationPrincipal CustomUserDetails user) {
        return svc.getTransactionsByUserId(user.getUserId());
    }

    @PostMapping
    public Transaction create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody Transaction tx) {
        return svc.addTransaction(user.getUserId(), tx);
    }

    @PutMapping("/{id}")
    public Transaction update(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer id,
            @RequestBody Transaction tx) {
        return svc.updateTransaction(user.getUserId(), id, tx);
    }

    @GetMapping("/{id}")
    public Transaction getOne(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer id) {
        return svc.getTransactionByUserIdAndId(user.getUserId(), id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer id) {
        svc.deleteTransaction(user.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decryptExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("password") String password) {
        try (InputStream in = file.getInputStream()) {
            List<List<List<String>>> data = svc.decryptAndParseExcel(in, password);
            return ResponseEntity.ok(data);

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("엑셀 처리 오류: " + e.getMessage());
        }
    }

}