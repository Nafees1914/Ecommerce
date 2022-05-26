package com.Ecommerce.Ecommerce.Controller;


import com.Ecommerce.Ecommerce.Service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logout")
public class LogoutController
{

    @Autowired
    AccessTokenService accessTokenService;


    @PostMapping("/")
    public ResponseEntity<String> logoutUser(HttpServletRequest request)
    {
        String token = accessTokenService.parseJwt(request);
        if (token == null) {
            return new ResponseEntity<>("Token Not Found", HttpStatus.BAD_REQUEST);
        }

        return accessTokenService.deleteToken(token);

    }


}
