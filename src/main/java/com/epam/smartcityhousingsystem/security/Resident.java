package com.epam.smartcityhousingsystem.security;

import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.House;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**@<code>Resident</code> represents a resident table.
 * It has <code>one to many</code> relationship with
 * <code>House</code> and <code>Advertisement</code> entities.
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
public class Resident extends User {
    @Column(unique = true)
    private long residentCode;

    @JsonBackReference
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private Set<House> houses = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Advertisement> advertisements = new HashSet<>();

    public void addAdvertisement(Advertisement advertisement) {
        advertisements.add(advertisement);
        advertisement.setResident(this);
    }
    public void removeAdvertisement(Advertisement advertisement){
        advertisements.remove(advertisement);
        advertisement.setResident(null);
    }

    public void addHouse(House house) {
        houses.add(house);
        house.setResident(this);
    }

    public void removeHouse(House house) {
        houses.remove(house);
        house.setResident(null);
    }
}
