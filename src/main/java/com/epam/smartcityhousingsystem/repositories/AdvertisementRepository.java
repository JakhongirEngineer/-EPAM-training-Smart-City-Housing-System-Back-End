package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement,Long> {
    Optional<Advertisement> findByUuid(String uuid);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE from Advertisement a WHERE a.uuid=?1"
    )
    Integer deleteByUuid(String uuid);

//    @Transactional
//    @Modifying
//    @Query(
//            value = "DELETE from Advertisement a WHERE a.id=?1"
//    )
//    Integer deleteById(long id);
}
