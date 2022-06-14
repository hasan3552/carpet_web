package com.company.controller;

import com.company.dto.profile.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String jwt) {
        Integer pId = JwtUtil.decode(jwt, ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.create(dto);
        return ResponseEntity.ok(profileDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> update(@PathVariable("id") Integer id,
                                             @RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String jwt) {
        JwtUtil.decode(jwt, ProfileRole.ADMIN);
        profileService.update(id, dto);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/detail/update")
    public ResponseEntity<ProfileDTO> detailUpdate(@RequestBody ProfileDTO dto,
                                                   @RequestHeader("Authorization") String jwt) {
        Integer pId = JwtUtil.decode(jwt);
        profileService.update(pId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String jwt,
                                        @PathVariable("id") Integer id){

        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.getProfileDTOById(id);

        return ResponseEntity.ok(profileDTO);
    }


    @GetMapping("")
    public ResponseEntity<?> getProfileList(@RequestHeader("Authorization") String jwt){

        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        List<ProfileDTO> profileDTOS = profileService.getAllProfileDTOById();

        return ResponseEntity.ok(profileDTOS);
    }

    @DeleteMapping("")
    public ResponseEntity<?> changeVisible(@RequestHeader("Authorization") String jwt,
                                           @RequestParam("id") Integer profileId){

        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.changeVisible(profileId);

        return ResponseEntity.ok(profileDTO);

    }

    // filter**********
}
