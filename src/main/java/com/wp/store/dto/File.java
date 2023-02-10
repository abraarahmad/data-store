package com.wp.store.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wp.store.constants.StatusConst;

@Entity
@Table(name = "FILE")
public class File {

    @Id
    String id;

    String name;

    String location;
    
    String contentType;
    
    Long size;
    
    String status;

	public File() {
    }

    public File(String id, String name, String location, String contentType, Long size) {
    	this.id = id;
        this.name = name;
        this.location = location;
        this.contentType = contentType;
        this.size = size;
        this.status = StatusConst.PRESENT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
