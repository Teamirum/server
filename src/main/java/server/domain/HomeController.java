package server.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/test")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok().body("Hello, World!");
    }

    @PostMapping("/test")
    public ResponseEntity<String> home2() {
        return ResponseEntity.ok().body("Hello, World!");
    }
}
