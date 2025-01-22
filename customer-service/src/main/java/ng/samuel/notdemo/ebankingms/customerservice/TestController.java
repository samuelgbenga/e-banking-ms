package ng.samuel.notdemo.ebankingms.customerservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class TestController {
    @GetMapping
    public String getUser() {
        return "Samuel Gbenga";
    }
}
