package com.doitmoney.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VerificationCodeDto {
    private String email;
    private String code;
}