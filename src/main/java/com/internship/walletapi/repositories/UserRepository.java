package com.internship.walletapi.repositories;

import com.internship.walletapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Query(value = "select user0_.id as id1_4_, user0_.created_at as created_2_4_, user0_.update_at as update_a3_4_, user0_.account_non_expired as account_4_4_, user0_.account_non_locked as account_5_4_, user0_.credentials_non_expired as credenti6_4_, user0_.email as email7_4_, user0_.enabled as enabled8_4_, user0_.firstname as firstnam9_4_, user0_.last_name as last_na10_4_, user0_.main_currency as main_cu11_4_, user0_.password as passwor12_4_, user0_.user_role_id as user_ro14_4_, user0_.user_name as user_na13_4_ from users user0_ where user0_.user_name=:usernameOrEmail OR user0_.user_name=:usernameOrEmail", nativeQuery=true)
    Optional<User> findUserByUsernameOrEmail(@Param(value = "usernameOrEmail") String usernameOrEmail);
}