package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Dto.Request.ChangePasswordDto;
import com.Ecommerce.Ecommerce.Dto.Request.SellerUpdateDto;
import com.Ecommerce.Ecommerce.Dto.Request.UpdateAddressDto;
import com.Ecommerce.Ecommerce.Entity.Address;
import com.Ecommerce.Ecommerce.Entity.Seller;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Repository.AddressRepository;
import com.Ecommerce.Ecommerce.Repository.SellerRepository;
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

import java.util.Date;

@Slf4j
@Service
public class SellerService
{
    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    MailSender mailSender;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<?> viewSellerProfile(String email)
    {

        if (userRepository.existsByEmail(email))
        {

            User seller = userRepository.findUserByEmail(email);
            Address address = addressRepository.findAddressById(seller.getId());


            return new ResponseEntity<>("Seller User Id: "+seller.getId()+
                    "\nSeller First name: "+seller.getFirstName()+
                    "\nSeller Last name: "+seller.getLastName()+
                    "\nSeller active status: "+seller.isActive()+
                    "\nSeller companyContact: "+sellerRepository.getCompanyContactByUserId(seller.getId())+
                    "\nSeller companyName: "+sellerRepository.getCompanyNameByUserId(seller.getId())+
                    "\nSeller gstNumber: "+sellerRepository.getGstNumberByUserId(seller.getId())+
                    "\nSeller Address: \n"+ address.toString(), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Error fetching addresses", HttpStatus.NOT_FOUND);
        }

    }


    public ResponseEntity<?> updateSellerProfile(SellerUpdateDto sellerUpdateDto, String email)
    {

        if (userRepository.existsByEmail(email)) {

            User user = userRepository.findUserByEmail(email);
            if(sellerUpdateDto.getFirstName()!=null)user.setFirstName(sellerUpdateDto.getFirstName());
            if(sellerUpdateDto.getLastName()!=null)user.setLastName(sellerUpdateDto.getLastName());
            if(sellerUpdateDto.getEmail()!=null)user.setEmail(sellerUpdateDto.getEmail());

            Seller seller = sellerRepository.getSellerByUserId(user.getId());
            if(sellerUpdateDto.getCompanyContact()!=null)seller.setCompanyContact(sellerUpdateDto.getCompanyContact());
            if(sellerUpdateDto.getCompanyName()!=null)seller.setCompanyName(sellerUpdateDto.getCompanyName());
            if(sellerUpdateDto.getGstNumber()!=null)seller.setGstNumber(sellerUpdateDto.getGstNumber());
            userRepository.save(user);
            sellerRepository.save(seller);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("nafeesahmad1914@gmail.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Your current profile is updated");
            mailMessage.setText("ALERT!, Your profile has been updated, If it was not you contact Admin asap.\nStay Safe, Thanks.");


            mailMessage.setSentDate(new Date());
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Current  profile updated!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not update the profile!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> changeSellerPassword(ChangePasswordDto changePasswordDto, String email)
    {

        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findUserByEmail(email);
            user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
            log.info("Changed password and encoded, then saved it.");
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
        } else  {
            log.info("Failed to change password!");
            return new ResponseEntity<>("Failed to change password!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateSellerAddress(Long id, UpdateAddressDto addAddressDto, String email)
    {
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findUserByEmail(email);
            log.info("user exists");

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
        }
        else
        {
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: "+id), HttpStatus.NOT_FOUND);
        }
    }
}
