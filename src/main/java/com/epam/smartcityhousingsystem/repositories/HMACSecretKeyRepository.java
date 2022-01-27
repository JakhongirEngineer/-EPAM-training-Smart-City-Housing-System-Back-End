package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.HMACSecretKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface HMACSecretKeyRepository extends JpaRepository<HMACSecretKey, Integer> {


    @Query(value = "select secret_key from hmacsecret_key where component_name=?1", nativeQuery = true)
    String takeHMACSecretKeyByComponentName(String componentName);
}
