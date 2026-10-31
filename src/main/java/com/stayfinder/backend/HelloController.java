package com.stayfinder.backend;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController{

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
    this.helloService = helloService;
   }
   
    @GetMapping("/hello")
    public String hello() {
    return helloService.getMessage();
   }

} 