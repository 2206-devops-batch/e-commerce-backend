package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(
  origins = "http://a771792005a2b4fc3be50a71e9f3c835-1575173433.us-east-1.elb.amazonaws.com:5000/",
  allowCredentials = "true"
)
public class PodMonitoringController {

  static String hostname = System.getenv("HOSTNAME");

  @GetMapping("/hostname")
  public ResponseEntity<String> getHostname() {
    return ResponseEntity.ok(hostname);
  }
}
