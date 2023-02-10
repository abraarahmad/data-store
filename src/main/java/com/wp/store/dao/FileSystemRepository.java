package com.wp.store.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import com.wp.store.config.ApplicationConfig;
import com.wp.store.exception.FileStorageException;

@Repository
public class FileSystemRepository {
	
	private final Path fileStorageLocation;
	
	@Autowired
	public FileSystemRepository(ApplicationConfig appConfig) {
		this.fileStorageLocation = Paths.get(appConfig.getStorageFolder()).toAbsolutePath().normalize();
		try {
			 Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored", e);
		}
	}
    
	public String save(byte[] content, String fileName, String fileId) throws IOException {

		Path targetLocation = this.fileStorageLocation.resolve(fileId + "-" + new Date().getTime() + "-" + fileName);
		Files.write(targetLocation, content);
		return targetLocation.toAbsolutePath().toString();
	}
    
    public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            // Handle access or file not found problems.
            throw new RuntimeException();
        }
    }
}
