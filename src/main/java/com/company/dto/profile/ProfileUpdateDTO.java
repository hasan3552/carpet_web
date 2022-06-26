package com.company.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDTO {

    private String name;
    private String surname;
    private String password;
    private String attachId;

}
