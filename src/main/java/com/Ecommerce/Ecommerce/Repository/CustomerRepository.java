package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>
{
    @Query(value = "select * from customer c where c.user_id = ?1",nativeQuery = true)
    Customer getCustomerByUserId(Long id);
    @Query(value="select c.contact from customer c where c.user_id = ?1",nativeQuery = true)
    String getContactByUserId(Long id);

    Optional<Customer> findByEmail(String email);
}
