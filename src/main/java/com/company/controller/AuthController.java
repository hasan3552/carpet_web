package com.company.controller;

import com.company.dto.ResponseInfoDTO;
import com.company.dto.VerificationDTO;
import com.company.dto.profile.ProfileLoginResponseDTO;
import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.service.AuthService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //@ApiOperation(value = "Verification Sms", notes = "Method for verification sms")
//    @PostMapping("/verification")
//    public ResponseEntity<String> verification(@RequestBody VerificationDTO dto) {
//       // log.info("Request for verification sms dto: {}", dto);
//        String response = authService.verification(dto);
//        return ResponseEntity.ok(response);
//    }
//
//    //@ApiOperation(value = "Resend phone", notes = "Method for resend")
//    @PostMapping("/resend/{phone}")
//    public ResponseEntity<?> resend(@ApiParam(value = "phone", required = true, example = "99891234567890")
//                                    @PathVariable String phone) {
//        ResponseInfoDTO response = authService.resendSms(phone);
//        log.info("Request for resend phone sms by phone: {}", phone);
//        return ResponseEntity.ok(response);
//    }
}
