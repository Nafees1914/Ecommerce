package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long>
{

    @Query(value = "SELECT a.city, a.state, a.country, a.zip_code, a.address_line, a.label from address a WHERE a.user_id = ?1", nativeQuery = true)
    List<Object[]> findByUserId(Long id);

    @Query(value = "SELECT * FROM address a WHERE a.user_id = ?1", nativeQuery = true)
    Address findAddressById(Long id);
}
