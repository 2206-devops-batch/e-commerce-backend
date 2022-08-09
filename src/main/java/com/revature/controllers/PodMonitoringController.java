package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(
  origins = "http://aac53e81081b042258fc80efa94a009c-104931072.us-east-1.elb.amazonaws.com:3000/",
  allowCredentials = "true"
)
public class PodMonitoringController {

  static String hostname = System.getenv("HOSTNAME");

  @GetMapping("/hostname")
  public ResponseEntity<String> getHostname() {
    return ResponseEntity.ok(hostname);
  }
}
