package com.crmshot.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Backend funcionando!";
    }
    
    @PostMapping("/login")
    public String login(@RequestBody String data) {
        return "Login endpoint funcionando! Dados: " + data;
    }
}
