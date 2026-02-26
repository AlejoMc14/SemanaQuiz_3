package com.logincaos.controller;

import com.logincaos.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService s;

    public AuthController(AuthService s) {
        this.s = s;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String u, @RequestParam String p) throws Exception {
        Map<String, Object> res = s.login(u, p);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestParam String u, @RequestParam String p, @RequestParam String e) throws Exception {
        Map<String, Object> res = s.register(u, p, e);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> res = new HashMap<>();
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }
}
