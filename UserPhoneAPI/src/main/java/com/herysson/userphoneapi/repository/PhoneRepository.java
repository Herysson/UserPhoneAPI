package com.herysson.userphoneapi.repository;

import com.herysson.userphoneapi.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}