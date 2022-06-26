package com.company.dto.profile;

import com.company.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileCreateDTO {

    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private ProfileRole role;
    private String attachId;

}
