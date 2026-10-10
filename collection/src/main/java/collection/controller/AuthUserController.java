package collection.controller;

import collection.dto.AuthenticationDTO;
import collection.dto.LoginResponseDTO;
import collection.dto.RegisterDTO;
import collection.service.AuthUserService;
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
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDTO data){
        this.authUserService.register(data);
        return ResponseEntity.ok("created");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid AuthenticationDTO data){
        return ResponseEntity.ok(this.authUserService.login(data));
    }
}
