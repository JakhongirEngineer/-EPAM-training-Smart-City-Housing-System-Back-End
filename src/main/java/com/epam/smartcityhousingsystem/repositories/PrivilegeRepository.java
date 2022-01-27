package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

}
