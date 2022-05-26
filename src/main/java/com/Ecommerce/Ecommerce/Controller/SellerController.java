package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import com.Ecommerce.Ecommerce.Dto.Request.ChangePasswordDto;
import com.Ecommerce.Ecommerce.Dto.Request.SellerUpdateDto;
import com.Ecommerce.Ecommerce.Dto.Request.UpdateAddressDto;
import com.Ecommerce.Ecommerce.Service.ProfileImageService;
import com.Ecommerce.Ecommerce.Service.SellerService;
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
@RequestMapping("/seller")
public class SellerController
{
    @Autowired
    SellerService sellerService;

    @Autowired
    ProfileImageService profileImageService;

    @Autowired
    UserServiceImpl userServiceimpl;



    @GetMapping("/profile")
    public ResponseEntity<?> viewProfile(HttpServletRequest request)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return sellerService.viewSellerProfile(email);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody SellerUpdateDto sellerUpdateDto,HttpServletRequest request)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return sellerService.updateSellerProfile(sellerUpdateDto,email);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return sellerService.changeSellerPassword(changePasswordDto,email);
    }

    @PutMapping("/update-address")
    public  ResponseEntity<?> updateAddress(@RequestParam("addressId") Long id, @RequestBody UpdateAddressDto updateAddressDto,HttpServletRequest request)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        return sellerService.updateSellerAddress(id, updateAddressDto,email);
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
