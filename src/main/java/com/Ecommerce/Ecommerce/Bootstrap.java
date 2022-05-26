package com.Ecommerce.Ecommerce;

import com.Ecommerce.Ecommerce.Entity.Role;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Repository.RoleRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@Transactional
public class Bootstrap implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        if(roleRepository.count()<1){
            Role admin = new Role();
            admin.setAuthority("ROLE_ADMIN");

            Role customer = new Role();
            customer.setAuthority("ROLE_CUSTOMER");

            Role seller = new Role();
            seller.setAuthority("ROLE_SELLER");

            roleRepository.save(admin);
            roleRepository.save(customer);
            roleRepository.save(seller);

        }

        if(userRepository.count()<1){
            User user = new User();
            user.setFirstName("Admin");
            user.setEmail("worldforu1914@gmail.com");
            user.setPassword(passwordEncoder.encode("AdminTtn@123"));
            user.setActive(true);
            user.setDeleted(false);
            user.setExpired(false);
            user.setLocked(false);
            user.setInvalidAttemptCount(0);

            Role role = roleRepository.findByAuthority("ROLE_ADMIN").get();
            user.setRoles(Collections.singletonList(role));
            userRepository.save(user);
        }
    }
}
