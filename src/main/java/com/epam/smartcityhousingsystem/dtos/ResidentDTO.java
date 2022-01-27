package com.epam.smartcityhousingsystem.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ApiModel(description = "Class that represents a resident without password or id")
@Builder
@ToString
@Setter
@Getter
public class ResidentDTO {
    @ApiModelProperty(notes = "code that every resident has and it is unique", example = "12345")
    private long residentCode;
    @ApiModelProperty(notes = "first name of a resident", example = "John")
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;
    @ApiModelProperty(notes = "last name of a resident", example = "Doa")
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;
    @ApiModelProperty(notes = "email of a resident. This is a required field", example = "myemail@gmail.com")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @ApiModelProperty(notes = "codes of houses that a resident owns")
    private Set<Long> houseCodes;
    @ApiModelProperty(notes = "UUIDs of advertisement that a resident owns")
    private Set<String> advertisementUUIDs;
}
