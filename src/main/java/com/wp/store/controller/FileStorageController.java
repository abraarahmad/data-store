package com.wp.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wp.store.dto.File;
import com.wp.store.payload.Response;
import com.wp.store.service.FileStorageService;

@RestController
@RequestMapping("file-system")
class FileStorageController {

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/files")
    Response uploadFile(@RequestParam MultipartFile file) throws Exception {
		String fileId = fileStorageService.save(file.getBytes(), file.getOriginalFilename(), file.getContentType(),
				file.getSize());
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileId)
                .toUriString();
        return new Response(file.getOriginalFilename(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping(value = "/files/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
    FileSystemResource downloadFile(@PathVariable String fileId) throws Exception {
        return fileStorageService.find(fileId);
    }
    
    @RequestMapping("/files")
    public List<Response> getAllFiles() {
    	List<File> allFiles = fileStorageService.findAll();
    	List<Response> responseList = new ArrayList<>();
    	for(File file : allFiles) {
    		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(file.getId())
                    .toUriString();
    		responseList.add(new Response(file.getName(), fileDownloadUri,
                file.getContentType(), file.getSize()));
    	}
    	return responseList;
    }
    
    @RequestMapping(method = RequestMethod.DELETE,  value = "/files/{fileId}")
    String deleteFile(@PathVariable String fileId) {
    	return fileStorageService.delete(fileId);
    }
}
