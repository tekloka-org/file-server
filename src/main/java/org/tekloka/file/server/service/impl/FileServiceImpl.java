package org.tekloka.file.server.service.impl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tekloka.file.server.constants.FileConstants;
import org.tekloka.file.server.service.FileService;


@Service
public class FileServiceImpl implements FileService{

	private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Value("${file.storage.path}")
	private String fileStoragePath;

	@Override
	public String uploadFile(String filePath, MultipartFile file) {			
		if(null != file) {
			var storageDirectory = new File(fileStoragePath + filePath);
		    if (! storageDirectory.exists()){
		    	storageDirectory.mkdirs();		        
		    }
		    String fileName =  fileStoragePath + filePath + file.getOriginalFilename();
			try {
				file.transferTo(new File(fileName));
				return filePath + file.getOriginalFilename();		
			} catch (IllegalStateException | IOException e) {
				logger.error("Not able to upload file", e);
			}
		}
		return null;
	}

	@Override
	public ResponseEntity<Resource> getFile(String fileLocation) {
		String[] fileLocationArray = fileLocation.split("\\/");
		String fileName = fileLocationArray[fileLocationArray.length-1];
		var file = new File(fileStoragePath + FileConstants.FILE_PATH_SEPERATOR +fileLocation);
		var path = Paths.get(file.getAbsolutePath());
		try {
			var resource = new ByteArrayResource(Files.readAllBytes(path));
			return generateFileResponse(fileName, file.length(), resource);
		} catch (IOException e) {
			logger.error("Not able to get file", e);
			return null;
		}
	}
	
	
	private ResponseEntity<Resource> generateFileResponse(String fileName, long contentLength, ByteArrayResource resource){
		var header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(contentLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
	}

	@Override
	public String checkServiceStatus() {
		return "File Server is up and running";
	}
}