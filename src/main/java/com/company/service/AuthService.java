package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.ResponseInfoDTO;
import com.company.dto.VerificationDTO;
import com.company.dto.profile.ProfileLoginResponseDTO;
import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.SmsEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.repository.ProfileRepository;
import com.company.repository.SmsRepository;
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Value("${server.url}")
    private String serverUrl;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SmsService smsService;


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
        if (profile.getPhoto() != null) {
            dto.setUrl(serverUrl + "attach/open?fileId=" + profile.getPhoto().getUuid());
        }

        return dto;
    }

    // in progress
    public ProfileDTO registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByPhoneNumber(dto.getPhoneNumber());

        if (optional.isPresent() && optional.get().getVisible() &&
                optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        if (optional.isPresent()) {
            entity = optional.get();
            entity.setStatus(ProfileStatus.ACTIVE);
            entity.setVisible(Boolean.TRUE);

        }
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setPhoneNumber(dto.getPhoneNumber());

        entity.setRole(ProfileRole.CUSTOMER);
        profileRepository.save(entity);

        smsService.sendRegistrationSms(dto.getPhoneNumber());

        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setName(dto.getName());
        responseDTO.setSurname(dto.getSurname());
        responseDTO.setPhoneNumber(dto.getPhoneNumber());
        responseDTO.setRole(ProfileRole.CUSTOMER);

        responseDTO.setJwt(JwtUtil.encode(entity.getId()));
        return responseDTO;
    }


    public String verification(VerificationDTO dto) {

        SmsEntity sms = smsService.getSmsByPhone(dto.getPhone());
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (!sms.getCode().equals(dto.getCode())) {

            sms.setRequestCount(sms.getRequestCount() + 1);
            smsService.save(sms);

            return "Code Invalid";
        }
        if (validDate.isBefore(LocalDateTime.now())) {

            return "Time is out";
        }

        if (sms.getRequestCount() > 4) {

            return "exceeded the limit";

        }
        profileRepository.updateStatusByPhone(dto.getPhone(), ProfileStatus.ACTIVE);
        return "Verification Done";
    }

    public ResponseInfoDTO resendSms(String phone) {
        Long count = smsService.getSmsCount(phone);
        if (count >= 4) {
            return new ResponseInfoDTO(-1, "Limit dan o'tib getgan");
        }

        smsService.sendRegistrationSms(phone);
        return new ResponseInfoDTO(1, "success");
    }
}
