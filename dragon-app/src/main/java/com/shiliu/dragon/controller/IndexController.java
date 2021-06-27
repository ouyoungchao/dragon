package com.shiliu.dragon.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
@Transactional
@RestController
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public String hello(){
        return "forward:index.html";
    }
}
