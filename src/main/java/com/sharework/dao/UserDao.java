package com.sharework.dao;

import com.sharework.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeleteYn(long id,String deleteYn);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByEmailAndDeleteYn(String email, String deleteYn);

    User getUserByPhoneNumber(String phoneNumber);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    Optional<User> findUserByPhoneNumberAndDeleteYn(String phoneNumber, String deleteYn);

    Optional<User> findUserByNameAndDeleteYn(String name, String deleteYn);

    Optional<User> findUserByPhoneNumberAndPassword(String phoneNumber, String password);

    Optional<User> findUserByPhoneNumberAndPasswordAndDeleteYn(String phoneNumber, String password,String deleteYn);

    Boolean existsByPhoneNumberAndDeleteYn(String name, String deleteYn);

    Boolean existsByNameAndDeleteYn(String phoneNumber, String deleteYn);

    Boolean existsByEmailAndDeleteYn(String email, String deleteYn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User u SET u.jwt = ?1 WHERE u.id = ?2")
    void changeJwt(String refreshToken, long userid);
}

