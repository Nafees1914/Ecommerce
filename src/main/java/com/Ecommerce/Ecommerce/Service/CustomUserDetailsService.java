package com.Ecommerce.Ecommerce.Service;


import com.Ecommerce.Ecommerce.Entity.Role;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService  implements UserDetailsService {

//    public static final int MAX_FAILED_ATTEMPTS = 3;

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities((List<Role>) user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    public int activateUser(String email) {
        return userRepository.activateUser(email);
    }

    public void deactivateUser(String email) {
        userRepository.deactivateUser(email);;
    }

    public void increaseFailedAttempts(Optional<User> user) {
        int newFailAttempts = user.get().getInvalidAttemptCount()+1;
        userRepository.updateInvalidAttemptCount(user.get().getEmail(),newFailAttempts);
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateInvalidAttemptCount(email, 0);
    }

    public void lock(Optional<User> user) {
        user.get().setLocked(false);
        userRepository.save(user.get());
    }

}