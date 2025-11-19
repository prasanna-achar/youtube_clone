package com.nts.users.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Healthcheck {


    @GetMapping("/auth")
    public static Map<String, String> init(){
        Map<String, String> res = new HashMap<>();
        res.put("message", "Hello server running fine");
        return res;
    }
}
