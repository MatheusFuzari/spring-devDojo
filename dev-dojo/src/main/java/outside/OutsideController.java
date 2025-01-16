package outside;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutsideController {

    @GetMapping("/outsite")
    public String outside() {
        return "Outside Controller";
    }
}
