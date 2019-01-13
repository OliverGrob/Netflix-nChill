package com.codecool.netflixandchill.repository;

import com.codecool.netflixandchill.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserByEmailAddress(String email);

}
