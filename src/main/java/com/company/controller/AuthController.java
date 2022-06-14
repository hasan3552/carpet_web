package com.company.controller;

import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@RequestBody AuthDTO dto) {

        ProfileDTO profileDTO = authService.login(dto);
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping("/registration")
    public ResponseEntity<ProfileDTO> registration(@RequestBody RegistrationDTO dto) {

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
