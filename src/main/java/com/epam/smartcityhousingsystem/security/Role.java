package com.epam.smartcityhousingsystem.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**@<code>Role</code> represents a role table.
 * It has <code>many to many</code> relationship with
 * <code>Privilege</code> and <code>User</code> entities.
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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "roles_priviliges",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    )
    private Set<Privilege> privileges;
}
