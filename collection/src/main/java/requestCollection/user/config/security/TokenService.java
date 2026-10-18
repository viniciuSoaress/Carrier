package requestCollection.user.config.security;

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

@Service
public class TokenService {

    @Value("${api.secret.jwt.token}")
    private String secret;

    private static final String issuer = "user_carrier_login";

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(User user){
        try{
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(getInstant())
                    .sign(this.getAlgorithm());
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while", exception);
        }
    }

    public String validateToken(String token){
        try{
            return JWT.require(this.getAlgorithm())
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant getInstant(){
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
