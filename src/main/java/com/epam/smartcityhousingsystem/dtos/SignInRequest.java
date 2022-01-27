package com.epam.smartcityhousingsystem.dtos;


import lombok.*;

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
public class SignInRequest {
    private String email;
    private String password;
}
