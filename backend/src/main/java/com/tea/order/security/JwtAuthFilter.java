package com.tea.order.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                LoginUser user = jwtUtil.parse(token);
                List<GrantedAuthority> authorities = new ArrayList<>();
                String sec = "ADMIN".equals(user.getType()) ? "ADMIN" : "CUSTOMER";
                authorities.add(new SimpleGrantedAuthority("ROLE_" + sec));
                String role = user.getRole();
                // 权限：店长可管员工、进工作台；店员可进工作台；客户仅个人中心
                if ("店长".equals(role) || "OWNER".equals(role) || "老板".equals(role)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                } else if ("店员".equals(role) || "STAFF".equals(role)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                }
                var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
