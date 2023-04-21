package com.example.demo.Controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Repositories.MT940MinRepository;

@RestController
@RequestMapping("customers")
public class MT940MinController {
	
	public enum FileType{
		
	}
	
	private final MT940MinRepository repo;
	
	MT940MinController(MT940MinRepository repo)
	{
		this.repo = repo;
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
				return "isCSV";
			case "application/xml":
				return "isXML";
			default: return "unknown file type";
		}
	}
}
