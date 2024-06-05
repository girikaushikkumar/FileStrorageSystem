package com.file.repository;

import com.file.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepo extends MongoRepository<File,String> {
}
