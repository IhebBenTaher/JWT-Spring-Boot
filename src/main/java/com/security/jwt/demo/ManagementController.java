package com.security.jwt.demo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {
    @GetMapping
    public String get(){
        return "management:get";
    }
    @PostMapping
    public String post(){
        return "management:post";
    }
    @PutMapping
    public String put(){
        return "management:put";
    }
    @DeleteMapping
    public String delete(){
        return "management:delete";
    }
}
