package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    Optional<House> findByCode(long code);
}
