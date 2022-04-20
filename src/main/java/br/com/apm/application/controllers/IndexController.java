package br.com.apm.application.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class IndexController {


    @GetMapping()
    public String home() {
        return "Olá Mundo";

    }

}

