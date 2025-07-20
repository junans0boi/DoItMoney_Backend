package com.doitmoney.backend.user.controller;

import com.doitmoney.backend.security.CustomUserDetails;
import com.doitmoney.backend.user.dto.ChangePasswordReq;
import com.doitmoney.backend.user.dto.UpdateUserReq;
import com.doitmoney.backend.user.dto.UserProfileDto;
import com.doitmoney.backend.user.service.FileStorageService;
import com.doitmoney.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    
    /** 이메일/전화번호 중복 체크 **/
    @GetMapping("/check-email")
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        return Map.of("available", !userService.existsEmail(email));
    }

    @GetMapping("/check-phone")
    public Map<String, Boolean> checkPhone(@RequestParam String phone) {
        return Map.of("available", !userService.existsPhone(phone));
    }

   

    /** 내 프로필 조회 **/
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile(Authentication auth) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
        UserProfileDto dto = userService.getProfile(userId);
        return ResponseEntity.ok(dto);
    }

    /** 내 프로필 수정 (username + image) **/
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDto> updateProfile(
            Authentication auth,
            @RequestPart("username") String username,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
        UpdateUserReq req = new UpdateUserReq();
        req.setUsername(username);
        if (image != null && !image.isEmpty()) {
            String url = fileStorageService.store(image);
            req.setProfileImageUrl(url);
        }
        UserProfileDto updated = userService.updateUser(userId, req);
        return ResponseEntity.ok(updated);
    }

    /** 내 비밀번호 변경 **/
    @PostMapping("/me/change-password")
    public ResponseEntity<Void> changePassword(
            Authentication auth,
            @RequestBody ChangePasswordReq req) {
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
        userService.changePassword(userId, req);
        return ResponseEntity.ok().build();
    }
}