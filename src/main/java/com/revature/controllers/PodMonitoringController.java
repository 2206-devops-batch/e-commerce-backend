package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(
  origins = "*",
  allowCredentials = "true"
)
public class PodMonitoringController {

  static String hostname = System.getenv("HOSTNAME");

  @GetMapping("/hostname")
  public ResponseEntity<String> getHostname() {
    return ResponseEntity.ok(hostname);
  }
}
