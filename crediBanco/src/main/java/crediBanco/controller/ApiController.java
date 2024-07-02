package crediBanco.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {


    @GetMapping("/data")
    public String getSecureData() {
        return "This is Spring Boot API secured data!";
    }

}
