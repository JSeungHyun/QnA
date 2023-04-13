package com.board.QnA.auth.userdetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberClaims {
    private String username;
    private List<GrantedAuthority> roles;
    private Long memberId;
}
