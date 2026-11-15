package requestCollection.user.config.security;

import org.springframework.security.core.GrantedAuthority;
import requestCollection.user.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.secret.jwt.token}")
    private String secret;

    private static final String ISSUER = "user_login";

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(User user){
        try{
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withClaim("roles", roles)
                    .withExpiresAt(getInstant())
                    .sign(this.getAlgorithm());

        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while", exception);
        }
    }

    private Instant getInstant(){
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
