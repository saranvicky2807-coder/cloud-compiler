package com.cloudcompiler.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaForwardController {

    @RequestMapping(value = {
            "/",
            "/login",
            "/register",
            "/dashboard",
            "/history",
            "/project/**"
    })
    public String forwardSpa() {
        return "forward:/index.html";
    }
}
