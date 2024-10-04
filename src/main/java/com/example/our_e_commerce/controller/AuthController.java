package com.example.our_e_commerce.controller;

import com.example.our_e_commerce.request.LoginRequest;
import com.example.our_e_commerce.responce.ApiResponse;
import com.example.our_e_commerce.responce.JwtResponse;
import com.example.our_e_commerce.security.JWT.JwtUtils;
import com.example.our_e_commerce.security.user.ShopUserDetails;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;;
    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
       try {
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getEmail(),
                           request.getPassword()
                   )
           );

           SecurityContextHolder.getContext().setAuthentication(authentication);

           String jwt = jwtUtils.generateTokenForUser(authentication);
           ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
           JwtResponse response = new JwtResponse(userDetails.getId(), jwt);
           return ResponseEntity.ok(new ApiResponse("Login Success", response));
       }catch (Exception e) {
           return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("Login Failed", e.getMessage()));
       }
    }

}
