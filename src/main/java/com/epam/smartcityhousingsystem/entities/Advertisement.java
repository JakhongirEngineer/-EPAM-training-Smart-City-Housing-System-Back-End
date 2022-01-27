package com.epam.smartcityhousingsystem.entities;

import com.epam.smartcityhousingsystem.entities.enums.AdvertisementState;
import com.epam.smartcityhousingsystem.security.Resident;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

/**
 * Advertisement Entity represents Advertisement table in the database.
 * It has one to one relationship with House Entity,
 * many to one relationship with Resident Entity,
 * and one to many relationship with Photo Entity.
 * All properties are required to be not null.
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
@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(updatable = false, unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String title;
    @NotNull(message = "price cannot be null")
    private double price;
    @NotNull(message = "description cannot be null")
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotNull(message = "phone cannot be null")
    private String phone;
    @NotNull(message = "advertisementState cannot be null")
    @Enumerated(EnumType.STRING)
    private AdvertisementState advertisementState;

    @JsonBackReference
    @OneToOne
    private House house;


    @JsonManagedReference
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.PERSIST, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<Photo> photos;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "residentId")
    private Resident resident;


    @PrePersist
    public void initializeUUID(){
        this.uuid = UUID.randomUUID().toString();

    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setAdvertisement(this);
    }
    public void removePhoto(Photo photo) {
        photos.remove(photo);
        photo.setAdvertisement(null);
    }
}


