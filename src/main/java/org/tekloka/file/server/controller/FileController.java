package org.tekloka.file.server.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.tekloka.file.server.service.FileService;

@RestController
@RequestMapping(value = "/file")
public class FileController {
	
	private final FileService fileService;
	
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@GetMapping(path = "/check-service-status")
	public String checkServiceStatus() {
		return fileService.checkServiceStatus();
	}
	
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadFile(@RequestParam(name = "filePath" ) String filePath, @RequestPart(name = "file") MultipartFile file) {		
	    return fileService.uploadFile(filePath, file);
	}
	
	@GetMapping(value = "/get/**")
	public ResponseEntity<Resource> getFile(HttpServletRequest request) {
		String fileLocation = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		fileLocation = fileLocation.replace("/file/get/", "");
		return fileService.getFile(fileLocation);
	}

}