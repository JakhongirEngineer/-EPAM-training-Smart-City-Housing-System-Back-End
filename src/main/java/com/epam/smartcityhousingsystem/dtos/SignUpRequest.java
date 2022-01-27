package com.epam.smartcityhousingsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ToString
@Setter
@Getter
public class SignUpRequest {
    @Email
    private String email;
    @NotNull(message = "password cannot be null")
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 20, message = "password size must be between 8 and 20")
    private String password;
    private String confirmationPassword;

    public SignUpRequest(@Email
                                 String email,
                         @NotNull(message = "password cannot be null")
                         @NotBlank(message = "password cannot be blank")
                         @Size(min = 8, max = 20, message = "password size must be between 8 and 20")
                                 String password,
                         String confirmationPassword) {
        this.email = email;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
    }

    public boolean isConfirmationPasswordTheSameAsPassword(){
        return this.password.equals(this.confirmationPassword);
    }
}
