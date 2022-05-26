package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Entity.Token.AccessToken;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Exception.ObjectNotFoundException;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AccessTokenService
{
    @Autowired
    AccessTokenRepository accessTokenRepository;

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return  null;
    }

    public User findUserByAccessToken(String token)
    {
        AccessToken accessToken = accessTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ObjectNotFoundException("Access Token does not exist"));

        return accessToken.getUser();
    }

    public void deleteByUser(User user)
    {
        accessTokenRepository.deleteByUser(user);
    }

    public ResponseEntity<String> deleteToken(String token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByToken(token);

        if(accessToken.isPresent())
        {
            User user = accessToken.get().getUser();
            accessTokenRepository.deleteByUser(user);
            return new ResponseEntity<>("Logout Successfully", HttpStatus.OK);

        }else
            return new ResponseEntity<>("Access Token Not Found",HttpStatus.BAD_REQUEST);
    }
}
