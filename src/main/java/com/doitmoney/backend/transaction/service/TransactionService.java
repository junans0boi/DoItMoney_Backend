package com.doitmoney.backend.transaction.service;

import com.doitmoney.backend.account.entity.Account;
import com.doitmoney.backend.account.repository.AccountRepository;
import com.doitmoney.backend.transaction.entity.Transaction;
import com.doitmoney.backend.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;

    public TransactionService(TransactionRepository txRepo,
            AccountRepository accountRepo) {
        this.txRepo = txRepo;
        this.accountRepo = accountRepo;
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return txRepo.findByUserId(userId);
    }

    public Transaction addTransaction(Long userId, Transaction tx) {
        // 1) PK(userId+id) 세팅
        tx.setUserId(userId);
        int nextId = txRepo.findByUserId(userId).stream()
                .map(Transaction::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
        tx.setId(nextId);

        // 2) Account 연관관계 설정
        Account acct = accountRepo
                .findByUserUserIdAndAccountNumber(userId, tx.getAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("계좌를 찾을 수 없습니다"));
        tx.setAccount(acct);
        // NEW ▶ 선택된 계좌의 번호도 함께 저장
        tx.setAccountNumber(acct.getAccountNumber());
        // 3) 거래 저장
        Transaction saved = txRepo.save(tx);

        // 4) 잔액 갱신
        acct.setBalance(acct.getBalance() + tx.getAmount());
        accountRepo.save(acct);

        return saved;
    }

    public Transaction getTransactionByUserIdAndId(Long userId, Integer id) {
        return txRepo.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "거래를 찾을 수 없습니다. userId=" + userId + ", id=" + id));
    }

    public Transaction updateTransaction(Long userId, Integer id, Transaction updated) {
        Transaction exist = getTransactionByUserIdAndId(userId, id);

        // 잔액 보정
        int delta = updated.getAmount() - exist.getAmount();

        // 필드 복사
        exist.setTransactionDate(updated.getTransactionDate());
        exist.setTransactionType(updated.getTransactionType());
        exist.setCategory(updated.getCategory());
        exist.setDescription(updated.getDescription());
        exist.setAmount(updated.getAmount());
        exist.setAccountName(updated.getAccountName());
        exist.setAccountNumber(updated.getAccountNumber());
        Transaction saved = txRepo.save(exist);

        // 잔액 적용
        Account acct = accountRepo
                .findByUserUserIdAndAccountNumber(userId, saved.getAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("계좌를 찾을 수 없습니다"));
        acct.setBalance(acct.getBalance() + delta);
        accountRepo.save(acct);

        return saved;
    }

    public void deleteTransaction(Long userId, Integer id) {
        Transaction tx = getTransactionByUserIdAndId(userId, id);

        // 잔액 복구
        // **수정: 계좌번호 기준으로 복구**
        accountRepo.findByUserUserIdAndAccountNumber(userId, tx.getAccountNumber())
                .ifPresent(acct -> {
                    acct.setBalance(acct.getBalance() - tx.getAmount());
                    accountRepo.save(acct);
                });
        txRepo.delete(tx);
    }

    public List<List<List<String>>> decryptAndParseExcel(InputStream in, String password) {
        try (Workbook workbook = WorkbookFactory.create(in, password)) {
            List<List<List<String>>> allSheets = new ArrayList<>();

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                List<List<String>> rows = new ArrayList<>();

                for (Row row : sheet) {
                    List<String> cells = new ArrayList<>();
                    for (Cell cell : row) {
                        cell.setCellType(CellType.STRING);
                        cells.add(cell.getStringCellValue());
                    }
                    rows.add(cells);
                }
                allSheets.add(rows);
            }
            return allSheets;
        } catch (EncryptedDocumentException e) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        } catch (IOException e) {
            throw new IllegalStateException("파일 처리 오류", e);
        }
    }
}