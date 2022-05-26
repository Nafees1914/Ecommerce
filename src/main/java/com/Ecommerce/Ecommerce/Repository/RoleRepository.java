package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer>
{
    Optional<Role> findByAuthority(String authority);
}
