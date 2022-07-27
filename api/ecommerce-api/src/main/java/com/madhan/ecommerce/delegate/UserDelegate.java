package com.madhan.ecommerce.delegate;

import java.util.UUID;

import com.madhan.ecommerce.dto.UserProviderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhan.ecommerce.dto.UserAuthentication;
import com.madhan.ecommerce.dto.UserLogin;
import com.madhan.ecommerce.dto.UserResponse;
import com.madhan.ecommerce.entity.User;
import com.madhan.ecommerce.exception.InvalidRequestDataException;
import com.madhan.ecommerce.exception.UserException;
import com.madhan.ecommerce.service.UserService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@Component
public class UserDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenUtil jwt;
    @Autowired
    ObjectMapper mapper;

    public User signUp(User user) {

        if (AppUtil.validate.test(user.getUsername())) {
            throw new InvalidRequestDataException("Username field cannot be null or empty");
        }
        emailValidate(user.getEmail(), user.getPassword());

        userService.validateEmailFromDB(user.getEmail());
        userService.validateUsernameFromDB(user.getUsername());
        setTimestampAndRoleForUser(user);
        return userService.saveUser(user);

    }

    public User getUser(String id, String authHeader) {

        jwt.authorizeRequest(authHeader);
        return userService.getUser(id);
    }

    public UserResponse login(UserLogin request){
        emailValidate(request.getEmail(), request.getPassword());
        User userObj = userService.userLogin(request);
        if (userObj == null)
            throw new UserException("Invalid Credentails", HttpStatus.UNAUTHORIZED);

        String token = createToken(userObj.getEmail(),userObj.getIsAdmin());
        return new UserResponse(userObj.getUsername(), userObj.getEmail(), token);
    }


    private void emailValidate(String email, String password) {

        if (AppUtil.validate.test(email)) {
            throw new InvalidRequestDataException("Email field cannot be null or empty");
        }
        if (!AppUtil.VALID_EMAIL.matcher(email).find()) {
            throw new InvalidRequestDataException("Invalid Email Id");
        }
        if (AppUtil.validate.test(password)) {
            throw new InvalidRequestDataException("password field cannot be null or empty");
        }


    }

    public UserResponse userProviderLogin(UserProviderDto request) {
       User userObj =  userService.checkUserProviderDetail(request.getEmail(),request.getProvider());
       if(userObj==null){
           User user = new User();
           setTimestampAndRoleForUser(user);
           user.setEmail(request.getEmail());
           user.setUsername(request.getUsername());
           user.setProvider(request.getProvider());
           userObj  = userService.saveUser(user);
       }
        String token = createToken(userObj.getEmail(),userObj.getIsAdmin());
        return new UserResponse(userObj.getUsername(), userObj.getEmail(), token);

    }

    private String createToken(String email, boolean isAdmin) {
        UserAuthentication auth = new UserAuthentication(email, isAdmin);
        String data = null;
        try {
            data = mapper.writeValueAsString(auth);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jwt.generateJWTToken(data);
    }

    private void setTimestampAndRoleForUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreatedAt(AppUtil.getDateTime());
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
    }
}
