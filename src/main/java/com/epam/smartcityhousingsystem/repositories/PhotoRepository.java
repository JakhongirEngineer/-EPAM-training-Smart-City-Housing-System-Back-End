package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.Photo;
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
public interface PhotoRepository extends JpaRepository<Photo,Long> {
//    Optional<Photo> findByUrl(String url);
//
//    @Transactional
//    @Modifying
//    @Query(
//            value = "DELETE from Photo p WHERE p.url=?1"
//    )
//    Integer deleteByUrl(String ulr);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE from Photo p WHERE p.advertisement.id=?1"
    )
    Integer deleteByAdvertisementId(long advertisementId);
}
