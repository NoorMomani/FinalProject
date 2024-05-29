package com.example.mentalhealthsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @GetMapping("/blogs")
    public String getBlog() {
        return "Blog";
    }
    @GetMapping("/{BlogName}")
    public String getBlogPage(@PathVariable("BlogName") String BlogName) {
        return BlogName;
    }
}
