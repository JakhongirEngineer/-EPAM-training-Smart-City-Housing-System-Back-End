package com.epam.smartcityhousingsystem.dtos;

import com.epam.smartcityhousingsystem.security.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(description = "Class that represents sign in response")

/**
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    @ApiModelProperty(notes = "object that illustrates a signed in user")
    private User user;
    @ApiModelProperty(notes = "JWT token for a signed in user")
    private String jwtToken;
    @ApiModelProperty(notes = "resident code that is used to identify a resident")
    private long residentCode;
}
