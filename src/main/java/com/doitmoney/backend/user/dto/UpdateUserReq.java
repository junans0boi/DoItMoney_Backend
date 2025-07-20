// src/main/java/com/Planairy/backend/user/dto/UpdateUserReq.java
package com.doitmoney.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserReq {
    private String username;
    private String profileImageUrl;
}