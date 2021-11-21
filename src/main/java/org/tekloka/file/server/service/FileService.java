package org.tekloka.file.server.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	ResponseEntity<Resource> getFile(String fileLocation);

	String uploadFile(String filePath, MultipartFile file);

	String checkServiceStatus();

}
