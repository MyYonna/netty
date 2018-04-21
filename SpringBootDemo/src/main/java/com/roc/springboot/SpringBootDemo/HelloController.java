package com.roc.springboot.SpringBootDemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(path="/hello")
    public String say(){
        return "hi you!!!";
    }
}
