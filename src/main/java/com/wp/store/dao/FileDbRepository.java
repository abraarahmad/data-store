package com.wp.store.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wp.store.dto.File;

@Repository
public interface FileDbRepository extends CrudRepository<File, String> {

	List<File> findAllByStatus(String status);

	File findByIdAndStatus(String fileId, String present);
}
