package ng.samuel.notdemo.ebankingms.authenticationservice.web;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passwords")
public class PasswordRestController {

//    private final PasswordService passwordService;
//
//    public PasswordRestController(PasswordService passwordService) {
//        this.passwordService = passwordService;
//    }
//
//    @GetMapping("/ask/{email}")
//    public void requestCodeToResetPassword(@PathVariable String email){
//        passwordService.requestCodeToResetPassword(email);
//    }
//
//    @PostMapping("/reset")
//    public void resetPassword(@RequestBody ChangePasswordRequestDTO dto){
//        passwordService.resetPassword(dto);
//    }

    @GetMapping
    public String getUser() {
        return "Samuel Gbenga";
    }
}
