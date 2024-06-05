package com.file.repository;

import com.file.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {
    Optional<User> findByUserName(String userName);
}
