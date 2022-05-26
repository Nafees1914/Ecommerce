package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.Customer;
import com.Ecommerce.Ecommerce.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer>
{
    @Query(value = "select s.company_contact from seller s  where s.user_id = ?1", nativeQuery = true)
    String getCompanyContactByUserId(Long id);

    @Query(value = "select s.company_name from seller s where s.user_id = ?1",nativeQuery = true)
    String getCompanyNameByUserId(Long id);

    @Query(value = "select s.gst_number from seller s where s.user_id = ?1",nativeQuery = true)
    String getGstNumberByUserId(Long id);

    @Query(value = "select * from seller s where s.user_id = ?1",nativeQuery = true)
    Seller getSellerByUserId(Long id);

    Optional<Seller> findByEmail(String email);
}
