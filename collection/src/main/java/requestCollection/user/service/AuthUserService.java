package requestCollection.user.service;

import requestCollection.user.config.security.TokenService;
import requestCollection.user.dto.AuthenticationDTO;
import requestCollection.user.dto.LoginResponseDTO;
import requestCollection.user.dto.RegisterDTO;
import requestCollection.user.entity.User;
import requestCollection.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public RegisterDTO register(RegisterDTO data){
        if (this.userRepository.findByEmail(data.email()) != null)
            throw new IllegalArgumentException("Login whit exist!");

        String passwordBCrypt = passwordEncoder.encode(data.password());

        User newuser = new User(data.name(), data.email(), passwordBCrypt, data.role());
        return new RegisterDTO(this.userRepository.save(newuser));
    }

    public LoginResponseDTO login(AuthenticationDTO data){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken( data.email(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = this.tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token, ((User) auth.getPrincipal()).getId());
    }
}
