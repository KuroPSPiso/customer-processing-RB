package com.example.demo.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.processing.ProcessMT940Min;
import com.example.demo.repositories.MT940MinRepository;
import com.example.demo.services.MT940MinService;

@RestController
@RequestMapping("customers")
public class MT940MinController {
	
	private final MT940MinService service;
	
	MT940MinController(MT940MinService service)
	{
		this.service = service;
	}
	
	@GetMapping("/test")
	public String test()
	{
		return "test";
	}
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file)
	{
		byte[] data;
		
		try {
			data = file.getBytes();
			if(data == null) throw new Exception("no data");
			if(data.length <= 0) throw new Exception("no data");
		} catch (IOException e) {
			return "Failed to upload: no content";
		} catch (Exception e) {
			return String.format("Failed to upload: {%s}", e.getMessage());
		}
		
		return this.processUploadTypes(file.getContentType(), data);
	}
	
	public String processUploadTypes(String type, byte[] contents)
	{
		type = type.toLowerCase();
		switch(type)
		{
			case "text/csv":
				return ProcessMT940Min.processCSV(contents, service).log;
			case "application/xml":
				return ProcessMT940Min.processXML(contents, service).log;
			default: return "unknown file type";
		}
	}
}
