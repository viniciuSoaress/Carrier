package requestCollection.user.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    public SecurityFilter(TokenService tokenService, AuthenticationService authenticationService) {
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);
        if(token != null){
            String login = tokenService.validateToken(token);
            if(login != null && !login.isBlank()){
                UserDetails user = authenticationService.loadUserByUsername(login);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        return (authHeader != null && authHeader.startsWith(TOKEN_PREFIX))
                ? authHeader.substring(TOKEN_PREFIX.length())
                : null;
    }
}
