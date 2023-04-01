package cn.jdblg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author jadonding
 */
@RestController
@RequestMapping
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }


}
