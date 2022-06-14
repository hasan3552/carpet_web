package com.company.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {

    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private String configPassword;
    private String login;
}
