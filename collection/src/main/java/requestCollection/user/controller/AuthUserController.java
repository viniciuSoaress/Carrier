package requestCollection.user.controller;

import domain.ModelsDto;
import requestCollection.user.service.AuthUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService userService) {
        this.authUserService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid ModelsDto.RegisterDto data){
        this.authUserService.register(data);
        return ResponseEntity.ok("created");
    }

    @PostMapping("/login")
    public ResponseEntity<ModelsDto.AuthenticationResponseDto> loginUser(@RequestBody @Valid ModelsDto.LoginDto data){
        return ResponseEntity.ok(this.authUserService.login(data));
    }
}
