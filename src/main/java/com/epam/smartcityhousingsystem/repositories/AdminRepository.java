package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.security.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
