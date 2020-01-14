package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RestEndPoints {
	
	@GetMapping("/close?hti={lookupkey}")
	public void close(@PathVariable String lookupKey) {
		
		System.out.println(" endpoint called " + lookupKey);
	}

}
