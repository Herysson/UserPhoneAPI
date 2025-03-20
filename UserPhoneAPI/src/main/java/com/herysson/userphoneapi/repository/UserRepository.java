package com.herysson.userphoneapi.repository;


import com.herysson.userphoneapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}