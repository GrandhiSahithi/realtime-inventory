package com.example.rtinv;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @GetMapping("/healthz")
  public String ok() {
    return "ok";
  }
}
