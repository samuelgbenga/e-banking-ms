package ng.samuel.notdemo.ebankingms.authenticationservice.web;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationRestController {

//    private final AuthenticationService authenticationService;
//
//    public AuthenticationRestController(AuthenticationService authenticationService) {
//        this.authenticationService = authenticationService;
//    }
//
//    @PostMapping("/login")
//    public LoginResponseDTO authenticate(@RequestBody LoginRequestDTO requestDTO) {
//        return authenticationService.authenticate(requestDTO);
//    }

    @GetMapping
    public String getUser() {
        return "Samuel Gbenga";
    }
}
