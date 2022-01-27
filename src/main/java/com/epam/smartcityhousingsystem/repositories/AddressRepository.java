package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
