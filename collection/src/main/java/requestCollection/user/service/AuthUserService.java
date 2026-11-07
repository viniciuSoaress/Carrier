package requestCollection.user.service;

import domain.ModelsDto;
import requestCollection.user.mapper.MapperModelsDto;
import requestCollection.user.config.security.TokenService;
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

    public ModelsDto.RegisterDto register(ModelsDto.RegisterDto data){
        if (this.userRepository.findByEmail(data.email()) != null)
            throw new IllegalArgumentException("Login whit exist!");

        String passwordBCrypt = passwordEncoder.encode(data.password());

        User newuser = new User(data.name(), data.email(), passwordBCrypt, data.role());
        return MapperModelsDto.toDto(this.userRepository.save(newuser));
    }

    public ModelsDto.AuthenticationResponseDto login(ModelsDto.LoginDto data){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken( data.email(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = this.tokenService.generateToken((User) auth.getPrincipal());

        return new  ModelsDto.AuthenticationResponseDto(token, ((User) auth.getPrincipal()).getId());
    }
}
