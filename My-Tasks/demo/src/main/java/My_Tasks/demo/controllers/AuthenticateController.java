package My_Tasks.demo.controllers;

import My_Tasks.demo.entities.DTOS.LoginDTO;
import My_Tasks.demo.entities.DTOS.RegisterDTO;
import My_Tasks.demo.entities.DTOS.UserDTO;
import My_Tasks.demo.entities.User;
import My_Tasks.demo.exceptions.AlreadyExistException;
import My_Tasks.demo.exceptions.NotFoundException;
import My_Tasks.demo.repositories.UserRepository;
import My_Tasks.demo.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticateController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticateController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register( @RequestBody RegisterDTO data){
        if(userRepository.existsByEmail(data.email())) throw new AlreadyExistException("Já existe um user cadastrado com este email");
        String passwordEncoded = passwordEncoder.encode(data.password());
        User newUser = new User(data.name(), data.email(), passwordEncoded);
        userRepository.save(newUser);
        return ResponseEntity.ok().body(new UserDTO(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login( @RequestBody LoginDTO data){
        if(!userRepository.existsByEmail(data.email())) throw new NotFoundException("Não existe um user cadastrado com este email");
        UsernamePasswordAuthenticationToken userPassword = new UsernamePasswordAuthenticationToken(data.email(),data.password());
        Authentication authentication = authenticationManager.authenticate(userPassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok().body(Map.of("token", token));
    }
}
