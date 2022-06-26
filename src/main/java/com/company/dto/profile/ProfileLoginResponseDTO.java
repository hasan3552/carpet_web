package com.company.dto.profile;

import com.company.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileLoginResponseDTO {

    private String name;
    private String surname;
    private String phoneNumber;

    private ProfileRole role;
    private String url;

    private String jwt;

}
