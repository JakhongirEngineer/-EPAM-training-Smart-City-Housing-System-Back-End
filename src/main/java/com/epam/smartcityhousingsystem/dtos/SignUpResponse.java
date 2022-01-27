package com.epam.smartcityhousingsystem.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ApiModel(description = "Class that represents response for signing up")

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SignUpResponse {
    @ApiModelProperty(notes = "success or error message for sign up", example = "you are successfully signed up")
    private String message;
    @ApiModelProperty(notes = "If it is successful, a resident gets a resident code", example = "1953463")
    private long residentCode;
}
