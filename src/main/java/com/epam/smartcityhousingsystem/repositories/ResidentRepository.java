package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.security.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface ResidentRepository extends JpaRepository<Resident,Long> {
    Optional<Resident> findByResidentCode(long residentCode);
    Optional<Resident> findByEmail(String email);
}
