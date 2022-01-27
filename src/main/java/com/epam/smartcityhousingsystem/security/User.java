package com.epam.smartcityhousingsystem.security;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**@<code>User</code> represents a security_user table.
 * It has <code>many to many</code> relationship with
 * <code>Role</code> entity.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "security_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;
    @NotBlank(message = "email cannot be blank")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "password cannot be blank")
    private String password;
    @NotNull(message = "enabled cannot be null")
    private boolean enabled;
    @NotNull(message = "tokenExpired cannot be null")
    private boolean tokenExpired;
    private boolean signedUp;
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;
}
