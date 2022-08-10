package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
// @CrossOrigin(origins = "http://a541fc074149e4506816882a4c7ef8e6-623895046.us-east-1.elb.amazonaws.com:3000", allowCredentials = "true")
@CrossOrigin(origins = "http://adebcf51e12fe4acc9f4deac43840e35-1727142081.us-east-1.elb.amazonaws.com:5000", allowCredentials = "true")
public class PodMonitoringController {

	static String hostname = System.getenv("HOSTNAME");

	@GetMapping("/hostname")
    public ResponseEntity<String> getHostname() {
        return ResponseEntity.ok(hostname);
    }
}
