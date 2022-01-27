package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.AdvertisementArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface AdvertisementArchiveRepository extends JpaRepository<AdvertisementArchive,Long> {
    //    @Query(value = "SELECT * FROM AdvertisementArchive WHERE AdvertisementArchive.advertisementArchiveUUID =?1", nativeQuery = true)
    @Query(value = "SELECT ar FROM AdvertisementArchive ar where LOWER(ar.uuid) = LOWER(:uuid)")
    Optional<AdvertisementArchive> findByUuid(@Param("uuid") String advertisementArchiveUUID);

    Integer deleteByUuid(@Param("uuid") String uuid);
}
