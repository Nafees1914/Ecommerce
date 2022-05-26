package com.Ecommerce.Ecommerce.Controller;


import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import com.Ecommerce.Ecommerce.Dto.Request.ChangePasswordDto;
import com.Ecommerce.Ecommerce.Dto.Request.CustomerUpdateDto;
import com.Ecommerce.Ecommerce.Dto.Request.UpdateAddressDto;
import com.Ecommerce.Ecommerce.Service.CustomerService;
import com.Ecommerce.Ecommerce.Service.ProfileImageService;
import com.Ecommerce.Ecommerce.Service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/customer")
public class CustomerController
{
    @Autowired
    CustomerService customerService;
    @Autowired
    ProfileImageService profileImageService;

    @Autowired
    UserServiceImpl userServiceimpl;

    @GetMapping("/profile")
    public ResponseEntity<?> viewProfile(HttpServletRequest request)
    {
       String token= userServiceimpl.getJwtFromRequest(request);
       String email = userServiceimpl.getUserNameFromJwtToken(token);
       return customerService.viewMyProfile(email);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @Valid @RequestBody CustomerUpdateDto updateCustomerDto)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return customerService.updateMyProfile(email, updateCustomerDto);
    }

    @GetMapping("/view-address")
    public ResponseEntity<?> viewAddress(HttpServletRequest request)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return customerService.viewMyAddresses(email);
    }


    @PutMapping("/update-address")
    public ResponseEntity<?> updateAddress(HttpServletRequest request, @RequestParam("addressId") Long id, @RequestBody UpdateAddressDto updateAddressDto)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return customerService.updateAddress(id,updateAddressDto);
    }

    @PutMapping("/add-address")
    public  ResponseEntity<?> addAddresses(HttpServletRequest request, @Valid @RequestBody UpdateAddressDto addAddressDto)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return customerService.addNewAddress(email, addAddressDto);
    }

    @DeleteMapping("/delete-address")
    public ResponseEntity<?> deleteAddress(HttpServletRequest request, @RequestParam("addressId") Long id)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return customerService.deleteAddress(id);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordDto changePasswordDto)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return customerService.changePassword(email, changePasswordDto);
    }


    @PostMapping(value = "/upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request)
            throws IOException
    {
        String mail = "";

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);

            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            mail = claims.getSubject();
        }
        return profileImageService.uploadImage(mail, image);
    }

    @GetMapping("/view-image")
    public ResponseEntity<?> listFilesUsingJavaIO(HttpServletRequest request)
    {

        String mail = "";
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
        {
            String token = bearerToken.substring(7);
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            mail = claims.getSubject();
        }
        return profileImageService.getImage(mail);
    }

}