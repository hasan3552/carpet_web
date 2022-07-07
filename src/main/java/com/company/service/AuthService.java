package com.company.service;

import com.company.config.CustomUserDetailService;
import com.company.config.CustomUserDetails;
import com.company.dto.profile.ProfileLoginResponseDTO;
import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.NoPermissionException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Value("${server.url}")
    private String serverUrl;
    @Autowired
    private AuthenticationManager authenticationManager;



    public ProfileLoginResponseDTO login(AuthDTO authDTO) {

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authDTO.getPhoneNumber(), authDTO.getPassword()));
        CustomUserDetails user = (CustomUserDetails) authenticate.getPrincipal();
        ProfileEntity profile = user.getProfile();

        ProfileLoginResponseDTO dto = new ProfileLoginResponseDTO();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setJwt(JwtUtil.encode(profile.getId()));
        dto.setRole(profile.getRole());
        if (profile.getPhoto() != null){
            dto.setUrl(serverUrl+"attach/open?fileId="+profile.getPhoto().getUuid());
        }

        return dto;
    }


    // in progress
    public ProfileDTO registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByPhoneNumber(dto.getPhoneNumber());

        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setPhoneNumber(dto.getPhoneNumber());

        entity.setRole(ProfileRole.CUSTOMER);
        profileRepository.save(entity);

        // name; surname; phone; password;

        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setName(dto.getName());
        responseDTO.setSurname(dto.getSurname());
        responseDTO.setPhoneNumber(dto.getPhoneNumber());
        responseDTO.setRole(ProfileRole.CUSTOMER);

        responseDTO.setJwt(JwtUtil.encode(entity.getId()));
        return responseDTO;
    }
}
