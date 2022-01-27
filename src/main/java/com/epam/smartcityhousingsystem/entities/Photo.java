package com.epam.smartcityhousingsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Photo Entity represents photo table in the database.
 * All properties are required.
 * It has many to one relationship with Advertisement, and it is the owning side.
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
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull(message = "url cannot be null")
    private String url;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "advertisementId")
    private Advertisement advertisement;


    public Photo(String url) {
        this.url = url;
    }

}
