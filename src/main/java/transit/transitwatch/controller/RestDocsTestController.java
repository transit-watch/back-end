package transit.transitwatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocsTestController {

    //rest docs test controller
    @GetMapping("rest")
    public String restDocsTestAPI() {
        return "test!!";
    }
}
