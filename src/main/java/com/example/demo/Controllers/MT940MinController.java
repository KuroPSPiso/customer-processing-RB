package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.*;

import com.example.demo.Repositories.MT940MinRepository;

@RestController
@RequestMapping("customers")
public class MT940MinController {
	
//	private final MT940MinRepository repo;
	
//	MT940MinController(MT940MinRepository repo)
//	{
//		this.repo = repo;
//	}
	
	@GetMapping("/test")
	public String test()
	{
		return "test";
	}
}
