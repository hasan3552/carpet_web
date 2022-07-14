package com.company.dto.profile;

import com.company.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileShortDTO {

    private Integer id;
    private String name;
    private String surname;
    private ProfileRole role;
    private String phoneNumber;
    private String attachUrl;
}
