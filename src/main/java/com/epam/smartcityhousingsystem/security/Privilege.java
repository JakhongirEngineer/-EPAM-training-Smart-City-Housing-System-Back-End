package com.epam.smartcityhousingsystem.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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
@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;
}
