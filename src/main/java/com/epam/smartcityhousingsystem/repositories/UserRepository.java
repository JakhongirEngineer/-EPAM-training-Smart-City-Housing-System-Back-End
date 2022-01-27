package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM security_user u where u.email=?1")
    Optional<List<User>> findByEmail(String email);
}
