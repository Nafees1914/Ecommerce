package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Repository.UserRepository;
import com.Ecommerce.Ecommerce.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RegistrationService registrationService;



    @GetMapping("/customer-list")
    public @ResponseBody List<Object[]> returnCustomers () {
        return userRepository.printPartialDataForCustomers();
    }

    @GetMapping("/seller-list")
    public @ResponseBody List<Object[]> returnSellers(HttpServletRequest httpServletRequest) {
        List<Object[]> list = new ArrayList<>();
        list.addAll(userRepository.printPartialDataForSellers());
        return list;
    }

    @PatchMapping("activate/customer/{id}")
    public ResponseEntity<?> activateCustomer(@PathVariable("id") Long id,HttpServletRequest request) {
           return registrationService.activateById(id,request);
    }

    @PatchMapping("/deactivate/customer/{id}")
    public ResponseEntity<?> deactivateCustomer(@PathVariable("id") Long id,HttpServletRequest request) {
        return registrationService.deactivateById(id,request);
    }

    @PatchMapping("/activate/seller/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable("id") Long id,HttpServletRequest request)
    {
        return registrationService.activateById(id,request);
    }

    @PatchMapping("/deactivate/seller/{id}")
    public ResponseEntity<?> deactivateSeller(@PathVariable("id") Long id,HttpServletRequest request) {
        return registrationService.deactivateById(id, request);
    }
}
