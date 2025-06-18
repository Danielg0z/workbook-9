package com.pluralsight.NorthwindTradersAPI.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // makes spring know that this should be responding to web requests
public class HomeController {
    //we're going to create a route

    @GetMapping("/")
    public String homePage(@RequestParam(defaultValue = "World") String country){
        return "Hello " + country;
    }

    @GetMapping("/daniel")
    public String danielPage(){
        return "Hello Daniel";
    }

}


