package com.internship.walletapi.repositories;

import com.internship.walletapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM USERS u WHERE u.email = :email", nativeQuery=true)
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM USERS u WHERE u.email = :email OR u.username = :email", nativeQuery=true)
    Optional<User> findUserByUsernameOrEmail(@Param("email") String email);
}
