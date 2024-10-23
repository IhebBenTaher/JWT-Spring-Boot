package com.security.jwt.demo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @GetMapping
    public String get(){
        return "admin:get";
    }
    @PostMapping
    public String post(){
        return "admin:post";
    }
    @PutMapping
    public String put(){
        return "admin:put";
    }
    @DeleteMapping
    public String delete(){
        return "admin:delete";
    }
}
