package br.com.sentinelx.core.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AgentApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final SentinelSecurityProperties securityProperties;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/v1/agent-events/**");

    public AgentApiKeyAuthenticationFilter(
            SentinelSecurityProperties securityProperties,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.securityProperties = securityProperties;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !requestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String headerName = securityProperties.getAgent().getApiKeyHeader();
        String providedApiKey = request.getHeader(headerName);

        if (!StringUtils.hasText(providedApiKey)
                || !providedApiKey.equals(securityProperties.getAgent().getApiKey())) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("Invalid or missing agent API key.")
            );
            return;
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                "agent",
                providedApiKey,
                List.of(new SimpleGrantedAuthority("ROLE_AGENT"))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}