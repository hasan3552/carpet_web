package com.company.controller;

import com.company.dto.profile.ProfileLoginResponseDTO;
import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

   // Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ProfileLoginResponseDTO> login(@RequestBody @Valid AuthDTO dto) {

        log.info("Request for login {}", dto);
        ProfileLoginResponseDTO profileDTO = authService.login(dto);
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping("/registration")
    public ResponseEntity<ProfileDTO> registration(@RequestBody @Valid RegistrationDTO dto) {

        ProfileDTO profileDTO = authService.registration(dto);
        return ResponseEntity.ok(profileDTO);
    }

//    @PostMapping("/validation")
//    public ResponseEntity<ProfileDTO> validation() {
//
//        ProfileDTO profileDTO = authService.registration(dto);
//        return ResponseEntity.ok(profileDTO);
//    }
}
