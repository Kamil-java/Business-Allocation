package pl.bak.businessallocationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private LocalDate birthDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(username, userDto.username) && Objects.equals(password, userDto.password) && Objects.equals(birthDate, userDto.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, username, password, birthDate);
    }
}
