package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    User findUserByEmail(String email);

    @Query(value = "select u.is_active from user u where u.id = ?1",nativeQuery = true)
    Boolean isUserActive(Long id);

    @Transactional
    @Modifying
    @Query(value = "update user u" +
            "set u.is_active = TRUE where u.email = ?1",nativeQuery = true)
    int activateUser(String email);

    @Transactional
    @Modifying
    @Query(value = "update user u" +
            " set u.is_active = 0 where u.email = ?1",nativeQuery = true)
    void deactivateUser(String email);

    @Transactional
    @Modifying
    @Query(value = "update user u" +
            " set u.invalid_attempt_count = ?2  where u.email = ?1",nativeQuery = true)
    void updateInvalidAttemptCount(String email, Integer invalidAttemptCount);

    @Query(value = "select u.id, u.first_name, u.last_name, u.email, u.is_active from user u where u.id = (SELECT user_id from user_role where role_id = 2)",nativeQuery = true)
    List<Object[]> printPartialDataForCustomers();


    @Query(value = "SELECT u.id, u.first_name, u.last_name, u.email, u.is_active, s.company_contact, s.company_name FROM user u, seller s WHERE u.id IN (SELECT user_id from user_role where role_id = 3) ", nativeQuery = true)
    List<Object[]> printPartialDataForSellers();

    @Query(value = "UPDATE user u SET u.failed_attempt = ?2 WHERE u.email = ?1",nativeQuery = true)
    @Modifying
    public void updateFailedAttempts(String email,int failAttempts);

    @Query(value = "select * from user where email = :email",nativeQuery = true)
    Optional<User> getUserByEmail(@Param("email") String email);
}

