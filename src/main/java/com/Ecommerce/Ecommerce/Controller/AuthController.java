package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import com.Ecommerce.Ecommerce.Dto.CustomerRegisterDto;
import com.Ecommerce.Ecommerce.Dto.LoginDto;
import com.Ecommerce.Ecommerce.Dto.Request.RefreshTokenRequestDto;
import com.Ecommerce.Ecommerce.Dto.SellerRegisterDto;
import com.Ecommerce.Ecommerce.Entity.Customer;
import com.Ecommerce.Ecommerce.Entity.Role;
import com.Ecommerce.Ecommerce.Entity.Seller;
import com.Ecommerce.Ecommerce.Entity.Token.AccessToken;
import com.Ecommerce.Ecommerce.Entity.Token.RefreshToken;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Exception.TokenRefreshException;
import com.Ecommerce.Ecommerce.Repository.CustomerRepository;
import com.Ecommerce.Ecommerce.Repository.RoleRepository;
import com.Ecommerce.Ecommerce.Repository.SellerRepository;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.AccessTokenRepository;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.RefreshTokenRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import com.Ecommerce.Ecommerce.Security.JwtGenerator;
import com.Ecommerce.Ecommerce.Service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    MailSenderService mailSenderService;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/home")
    public ResponseEntity<?> Home()
    {
        return ResponseEntity.ok("Welcome to Ecommerce 24*7");
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerAsCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto)
    {
        if(userRepository.existsByEmail(customerRegisterDto.getEmail()))
        {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        Customer customer =new Customer();
        customer.setFirstName(customerRegisterDto.getFirstName());
        customer.setLastName(customerRegisterDto.getLastName());
        customer.setContact(customerRegisterDto.getContact());
        customer.setEmail(customerRegisterDto.getEmail());
        customer.setPassword(passwordEncoder.encode(customerRegisterDto.getPassword()));
        customer.setActive(false);
        customer.setDeleted(false);
        customer.setExpired(false);
        customer.setLocked(false);
        customer.setInvalidAttemptCount(0);

        Role role = roleRepository.findByAuthority("ROLE_CUSTOMER").get();
        customer.setRoles(Collections.singletonList(role));
        customerRepository.save(customer);

        String generatedToken = registrationService.generateToken(customer);
        mailSenderService.setToEmail(customer.getEmail());
        mailSenderService.setSubject(customer.getFirstName() + " Your account activation Link");
        mailSenderService.setBody("Your Account activation link \n" + " please activate within 3 hours \n"
                + "127.0.0.1:8080/home/confirm/" + customer.getEmail() + "/" + generatedToken);

        logger.info(" Token sent through mail :  " + generatedToken );
        mailSenderService.sendEmail();

        return new ResponseEntity<>("Customer Registered Successfully! Check Your Mail To Activate Your Account", HttpStatus.CREATED);
    }
    @PostMapping("/register/seller")
    public ResponseEntity<?> registerAsSeller(@Valid @RequestBody SellerRegisterDto sellerRegisterDto)
    {
        if(userRepository.existsByEmail(sellerRegisterDto.getEmail()))
        {
            return new ResponseEntity<>("Email already taken!",HttpStatus.BAD_REQUEST);
        }

        Seller seller = new Seller();
        seller.setFirstName(sellerRegisterDto.getFirstName());
        seller.setLastName(sellerRegisterDto.getLastName());
        seller.setCompanyContact(sellerRegisterDto.getCompanyContact());
        seller.setCompanyName(sellerRegisterDto.getCompanyName());
        seller.setGstNumber(sellerRegisterDto.getGstNumber());
        seller.setEmail(sellerRegisterDto.getEmail());
        seller.setPassword(passwordEncoder.encode(sellerRegisterDto.getPassword()));
        seller.setActive(false);
        seller.setDeleted(false);
        seller.setExpired(false);
        seller.setLocked(false);
        seller.setInvalidAttemptCount(0);

        Role role = roleRepository.findByAuthority("ROLE_SELLER").get();
        seller.setRoles(Collections.singletonList(role));
        sellerRepository.save(seller);

        mailSenderService.setToEmail("worldforu1914@gmail.com");
        mailSenderService.setSubject("Please verify and activate id : " + seller.getId());
        mailSenderService.setBody("Hi Admin ! "  +", Above Seller account has been created. Waiting for your approval." + " Seller User Id is : " + seller.getId());
        mailSenderService.sendEmail();


        return new ResponseEntity<>("Account has been successfully created! Waiting for approval",HttpStatus.CREATED);
    }
//
//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto)
//    {
//        String requestedToken = refreshTokenRequestDto.getRefreshToken();
//        return refreshTokenService.findByToken(requestedToken)
//                .map(refreshTokenService ::checkExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String newToken = jwtGenerator.generateTokenFromUsername(user.getEmail());
//                    AccessToken accessToken = new AccessToken();
//                    accessToken.setToken(newToken);
//                    accessToken.setCreatedAt(LocalDateTime.now());
//                    accessToken.setExpiresAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_REFRESH_EXPIRATION));
//                    accessToken.setUser(user);
//                    return new ResponseEntity<>("New Access Token: "+ accessToken.getToken() + "\nRefresh Token : "+requestedToken +" ",HttpStatus.OK);
//                })
//                .orElseThrow(()-> new TokenRefreshException(requestedToken, "Refresh token is not in database."));
//    }


    /*
    * customer account confirm after successfully creating account
    */
    @PutMapping(path = "/customer/account-confirm")
    public String confirmByToken(@RequestParam("token") String token)
    {
        return registrationService.confirmByToken(token);
    }

    /*
    * Confirm email & Send reactivation link if previous token expired
    */
    @PostMapping(path = "/token/re-activation")
    public String confirmByEmail(@RequestParam("email") String email)
    {
        return registrationService.reactivationByEmail(email);
    }


    //Login Related Methods
    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAsAdmin(@Valid @RequestBody LoginDto loginDto)
    {
        User admin = userRepository.findUserByEmail(loginDto.getEmail());

        if (userRepository.isUserActive(admin.getId()))
        {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AccessToken accessToken = new AccessToken();
            accessToken.setToken(jwtGenerator.generateJwtToken(authentication));
            accessToken.setCreatedAt(LocalDateTime.now());
            accessToken.setExpiresAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_LOGIN_EXPIRATION));
            accessToken.setUser(admin);
            accessTokenRepository.save(accessToken);

            //RefreshToken refreshToken = refreshTokenService.generateRefreshToken(admin.getId());
            //refreshTokenRepository.save(refreshToken);

            String welcomeMessage = "Hello Admin, You are logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\n" +
                    "Access Token: " + accessToken.getToken() + "\n"
                    //+ "Refresh Token: " + refreshToken.getToken()
                    , HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/customer/login")
    public ResponseEntity<?> loginAsCustomer(@Valid @RequestBody LoginDto loginDto) {

        User customer = userRepository.findUserByEmail(loginDto.getEmail());

        if (userRepository.isUserActive(customer.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AccessToken accessToken = new AccessToken();
            accessToken.setToken(jwtGenerator.generateJwtToken(authentication));
            accessToken.setCreatedAt(LocalDateTime.now());
            accessToken.setExpiresAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_LOGIN_EXPIRATION));
            accessToken.setUser(customer);
            accessTokenRepository.save(accessToken);

//            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(customer.getId());
//            refreshTokenRepository.save(refreshToken);
            String welcomeMessage = "Hello! You have logged in successfully as a Customer!!";
            return new ResponseEntity<>(welcomeMessage +
                    "\nAccess Token: " + accessToken.getToken() +
                    "\nRefresh Token: "
                //    + refreshToken.getToken()
                    ,

                    HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/seller/login")
    public ResponseEntity<?> loginAsSeller(@Valid @RequestBody LoginDto loginDto) {

        User seller = userRepository.findUserByEmail(loginDto.getEmail());

        if (userRepository.isUserActive(seller.getId()))
        {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AccessToken accessToken = new AccessToken();
            accessToken.setToken(jwtGenerator.generateJwtToken(authentication));
            accessToken.setCreatedAt(LocalDateTime.now());
            accessToken.setExpiresAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_LOGIN_EXPIRATION));
            accessToken.setUser(seller);
            accessTokenRepository.save(accessToken);

//            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(seller.getId());
//            refreshTokenRepository.save(refreshToken);

            String welcomeMessage = "Hello! You have logged in successfully as a Seller!!";
            return new ResponseEntity<>(welcomeMessage +
                    "\nAccess Token: " + accessToken.getToken() +
                    "\nRefresh Token: "
                    // + refreshToken.getToken()+
                    ,
                    HttpStatus.OK);

        }
        else
        {
            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);
        }

    }
}
