package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingsController {

    @GetMapping("/{name}")
    public String helloName(@PathVariable String name){
        return "Hello, " + name + "!";
    }

    @GetMapping
    public String helloWorld(){
        return helloName("World");
    }
}
