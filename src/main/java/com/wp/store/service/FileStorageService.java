package com.wp.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.wp.store.constants.StatusConst;
import com.wp.store.dao.FileDbRepository;
import com.wp.store.dao.FileSystemRepository;
import com.wp.store.dto.File;
import com.wp.store.exception.FileNotFoundException;
import com.wp.store.exception.FileStorageException;

@Service
public class FileStorageService {

    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    FileDbRepository fileDbRepository;

	public String save(byte[] bytes, String fileName, String contentType, long fileSize) throws Exception {
		try {
			String fileId = UUID.randomUUID().toString();
			String location = fileSystemRepository.save(bytes, fileName, fileId);
			File savedFile = fileDbRepository.save(new File(fileId, fileName, location, contentType, fileSize));
			return savedFile.getId();
		} catch(Exception ex) {
    		throw new FileStorageException("Could not store file" + fileName + ". Please try again!", ex);
    	}
	}

    public FileSystemResource find(String fileId) {
    	try {
    		File file = fileDbRepository.findByIdAndStatus(fileId, StatusConst.PRESENT);
    	    return fileSystemRepository.findInFileSystem(file.getLocation());
    	}catch(Exception ex) {
    		throw new FileNotFoundException("File not found " + fileId, ex);
    	}
    }

	public List<File> findAll() {
		List<File> files = new ArrayList<>();
		fileDbRepository.findAllByStatus(StatusConst.PRESENT).forEach(files::add);
		return files;
	}

	public String delete(String fileId) {
		// soft delete only. Garbage Collector implementation required for hard delete
		try {
			File file = fileDbRepository.findById(fileId).get();
			if(file.getStatus().equals(StatusConst.PRESENT)) {
				file.setStatus(StatusConst.DELETED);
				fileDbRepository.save(file);
			}
			return file.getId();
		} catch(Exception ex) {
			throw new FileNotFoundException("File not found " + fileId, ex);
		}
	}

}
