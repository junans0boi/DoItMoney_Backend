package com.doitmoney.backend.security;

import com.doitmoney.backend.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user; // 실제 User 엔티티

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 스프링 시큐리티에서 요구하는 메서드들
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한이 없다고 가정
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 암호화된 패스워드
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 유저네임 = 이메일
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }

    // 우리 비즈니스 로직용 메서드
    public Long getUserId() {
        return user.getId();
    }
    
    public User getUser() {
        return user;
    }
}