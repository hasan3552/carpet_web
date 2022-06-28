package com.company.controller;

import com.company.dto.profile.ProfileCreateDTO;
import com.company.dto.profile.ProfileUpdateDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

//=========================== ADMIN ===============================
    @PostMapping("")
    public ResponseEntity<ProfileDTO> create(@RequestBody @Valid ProfileCreateDTO dto,
                                             @RequestHeader("Authorization") String jwt) {
        Integer pId = JwtUtil.decode(jwt, ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.create(dto);
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> update(@PathVariable("id") Integer id,
                                             @RequestBody @Valid ProfileCreateDTO dto,
                                             @RequestHeader("Authorization") String jwt) {
        JwtUtil.decode(jwt, ProfileRole.ADMIN);
        ProfileDTO update = profileService.update(id, dto);
        return ResponseEntity.ok(update);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String jwt,
                                        @PathVariable("id") Integer id){

        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.getProfile(id);

        return ResponseEntity.ok(profileDTO);
    }

    @GetMapping("")
    public ResponseEntity<?> getProfileList(@RequestHeader("Authorization") String jwt){

        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        List<ProfileDTO> profileDTOS = profileService.getAllProfileDTO();

        return ResponseEntity.ok(profileDTOS);
    }

    @DeleteMapping("/adm")
    public ResponseEntity<?> changeVisible(@RequestHeader("Authorization") String jwt,
                                           @RequestParam("id") Integer profileId){

        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.changeVisible(profileId);

        return ResponseEntity.ok(profileDTO);

    }

    //========================== EMPLOYEE ===============================

    @PutMapping("/update")
    public ResponseEntity<ProfileDTO> detailUpdate(@RequestBody @Valid ProfileUpdateDTO dto,
                                                   @RequestHeader("Authorization") String jwt) {
        Integer pId = JwtUtil.decode(jwt);
        ProfileDTO update = profileService.update(pId, dto);
        return ResponseEntity.ok(update);
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String jwt){

        Integer id = JwtUtil.decode(jwt);
        ProfileDTO profileDTO = profileService.getProfile(id);

        return ResponseEntity.ok(profileDTO);
    }

    @DeleteMapping("")
    public ResponseEntity<?> changeVisible(@RequestHeader("Authorization") String jwt){

        Integer profileId = JwtUtil.decode(jwt);
        ProfileDTO profileDTO = profileService.changeVisible(profileId);

        return ResponseEntity.ok(profileDTO);

    }
}
