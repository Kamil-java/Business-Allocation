package pl.bak.businessallocationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String birthDate;

}
