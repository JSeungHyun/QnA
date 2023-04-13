package com.board.QnA.auth.filter;

import com.board.QnA.auth.jwt.JwtTokenizer;
import com.board.QnA.auth.utils.CustomAuthorityUtils;
import com.board.QnA.exception.BusinessLogicException;
import com.board.QnA.exception.ExceptionCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> claims = verifyJws(request);
        setAuthenticationToContext(claims);
        filterChain.doFilter(request, response);
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) throws ServletException{
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        // Claims를 파싱할 수 있으면 서명 검증 역시 성공했다는 의미
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String,Object> claims){
        String username = (String) claims.get("username");
        Object memberIdObj = claims.get("memberId");
        Long memberId = memberIdObj != null ? ((Number) memberIdObj).longValue() : null;
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("memberId", memberId);

        List<GrantedAuthority> authorityList = authorityUtils.createAuthorities((List)claims.get("roles"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(map, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 특정 조건에 부합하면 실행
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }
}
