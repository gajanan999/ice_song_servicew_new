package com.ice.songservice.dao;

import com.ice.songservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailId(String emailId);

    boolean existsByUserName(String userName);

    User findByMobileNumberAndPassword(String mobileNumber, String password);
}
