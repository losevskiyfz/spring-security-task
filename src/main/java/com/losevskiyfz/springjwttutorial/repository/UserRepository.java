package com.losevskiyfz.springjwttutorial.repository;

import com.losevskiyfz.springjwttutorial.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
}