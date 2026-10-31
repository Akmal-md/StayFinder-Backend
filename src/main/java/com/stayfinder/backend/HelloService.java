package com.stayfinder.backend;
import org.springframework.stereotype.Service;

@Service
public class HelloService{

    public String getMessage() {
    return "Hello from HelloService!";
   }

}