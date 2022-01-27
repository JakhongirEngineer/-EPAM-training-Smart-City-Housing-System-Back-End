package com.epam.smartcityhousingsystem.dtos;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ApiModel(description = "Class that represents Advertisement without extra information")

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdvertisementDTO {
    private String uuid;
    @NotBlank(message = "Title cannot be blank")
    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "price cannot be null")
    private double price;
    @NotBlank(message = "description cannot be blank")
    @NotNull(message = "description cannot be null")
    private String description;
    @NotBlank(message = "phone cannot be blank")
    @NotNull(message = "phone cannot be null")
    private String phone;
    @NotNull(message = "houseCode cannot be null")
    private long houseCode;
    private Set<String> photoUrls;
    @NotNull(message = "residentCode cannot be null")
    private long residentCode;
}
