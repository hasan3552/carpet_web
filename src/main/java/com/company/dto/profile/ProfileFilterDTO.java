package com.company.dto.profile;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileFilterDTO {

    private String name;
    private String surname;
    private String phoneNumber;
    private String createdDateFrom;
    private String createdDateTo;
    private ProfileStatus status;
    private ProfileRole role;

}
