package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Dto.Request.ChangePasswordDto;
import com.Ecommerce.Ecommerce.Dto.Request.CustomerUpdateDto;
import com.Ecommerce.Ecommerce.Dto.Request.UpdateAddressDto;
import com.Ecommerce.Ecommerce.Entity.Address;
import com.Ecommerce.Ecommerce.Entity.Customer;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Repository.AddressRepository;
import com.Ecommerce.Ecommerce.Repository.CustomerRepository;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.AccessTokenRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
@Transactional
public class CustomerService
{

    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;
    @Autowired
    AddressRepository addressRepository;

    public ResponseEntity<?> viewMyProfile(String email)
    {

        User user = userRepository.findUserByEmail(email);
        return new ResponseEntity<>("Customer User Id: "+user.getId() +
                "\nCustomer First name: "+user.getFirstName()+
                "\nCustomer Last name: "+user.getLastName()+
                "\nCustomer active status: "+user.isActive()+
                "\nCustomer contact: " + customerRepository.getContactByUserId(user.getId()), HttpStatus.OK);
    }


    public ResponseEntity<?> changePassword(String email,ChangePasswordDto changePasswordDto)
    {

        if (userRepository.existsByEmail(email))
        {
            User user = userRepository.findUserByEmail(email);
            user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
            log.info("Password is saved after changed and encoded.");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("nafeesahmad1914@gmail.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Password Changed");
            mailMessage.setText("ALERT!, Your account's password has been changed, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setSentDate(new Date());

            try
            {
                mailSender.send(mailMessage);
            }
            catch (MailException e)
            {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Changed Password Successfully!", HttpStatus.OK);
        }
        else
        {
            log.info("Failed to change password!");
            return new ResponseEntity<>("Failed to change password!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addNewAddress(String email, UpdateAddressDto addAddressDto)
    {

        if (userRepository.existsByEmail(email))
        {
            User user = userRepository.findUserByEmail(email);
            log.info("user exists");

            Address address = new Address();
            address.setUser(user);
            address.setAddressLine(addAddressDto.getAddressLine());
            address.setCity(addAddressDto.getCity());
            address.setCountry(addAddressDto.getCountry());
            address.setState(addAddressDto.getState());
            address.setZipCode(addAddressDto.getZipcode());
            address.setLabel(addAddressDto.getLabel());
            addressRepository.save(address);
            log.info("Address added to the respected user");
            return new ResponseEntity<>("Added the address.", HttpStatus.CREATED);
        }
        else
        {
            log.info("Failed to add address.");
            return new ResponseEntity<>("Unable to add address!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateAddress(Long id, UpdateAddressDto addAddressDto)
    {

        if (addressRepository.existsById(id)) {
            log.info("address exists");
            Address address = addressRepository.getById(id);
            if(addAddressDto.getAddressLine() !=null)address.setAddressLine(addAddressDto.getAddressLine());
            if(addAddressDto.getLabel() !=null)address.setLabel(addAddressDto.getLabel());
            if(addAddressDto.getZipcode() !=null)address.setZipCode(addAddressDto.getZipcode());
            if(addAddressDto.getCountry() !=null)address.setCountry(addAddressDto.getCountry());
            if(addAddressDto.getState() !=null)address.setState(addAddressDto.getState());
            if(addAddressDto.getCity() !=null)address.setCity(addAddressDto.getCity());
            log.info("trying to save the updated address");
            addressRepository.save(address);
            return new ResponseEntity<>("Address updated successfully.", HttpStatus.OK);
        }
        else
        {
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: "+id), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteAddress(Long id)
    {

        if (addressRepository.existsById(id))
        {
            log.info("Address exists.");
            addressRepository.deleteById(id);
            log.info("deletion successful");
            return new ResponseEntity<>("Deleted Address Successfully.", HttpStatus.OK);
        }
        else
        {
            log.info("deletion failed!");
            return new ResponseEntity<>(String.format("No address found with associating address id: ", id), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> viewMyAddresses(String email)
    {

        if (userRepository.existsByEmail(email))
        {
            log.info("User exists!");
            User user = userRepository.findUserByEmail(email);
            List<Object[]> list = addressRepository.findByUserId(user.getId());
            log.info("returning a list of objects.");
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        else {
            log.info("Couldn't find address related to user!!!");
            return new ResponseEntity<>("Error fetching addresses", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> updateMyProfile(String email, CustomerUpdateDto updateCustomerDto)
    {

        if (userRepository.existsByEmail(email))
        {
            log.info("User exists.");
            Customer user = customerRepository.findByEmail(email).get();
            if(updateCustomerDto.getFirstName()!=null)user.setFirstName(updateCustomerDto.getFirstName());
            if(updateCustomerDto.getLastName()!=null) user.setLastName(updateCustomerDto.getLastName());
            if(updateCustomerDto.getEmail()!=null) user.setEmail(updateCustomerDto.getEmail());

            if(updateCustomerDto.getContact()!=null)user.setContact(updateCustomerDto.getContact());

            customerRepository.save(user);
            log.info("user updated!");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Profile Updated");
            mailMessage.setText("ALERT!, Your profile has been updated, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("nafeesahmad1914@gmail.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("User profile updated!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not update the profile!", HttpStatus.BAD_REQUEST);
        }
    }


}
