package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.entities.Photo;
import com.epam.smartcityhousingsystem.entities.enums.AdvertisementState;
import com.epam.smartcityhousingsystem.exceptions.ResidentDoesNotOwnTheAdvertisementException;
import com.epam.smartcityhousingsystem.exceptions.ResidentDoesNotOwnTheHouseException;
import com.epam.smartcityhousingsystem.repositories.AdvertisementRepository;
import com.epam.smartcityhousingsystem.repositories.PhotoRepository;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */


@AllArgsConstructor
@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final PhotoRepository photoRepository;
    private final ResidentRepository residentRepository;
    private final HousingSystemUtils housingSystemUtils;

    /**
     *
     * @param residentCode represents resident code that is used to uniquely identify a resident
     * @param advertisementDto represents AdvertisementDto
     * @return boolean that specifies if an advertisement is created or not
     */
    public boolean createAdvertisement(long residentCode, AdvertisementDTO advertisementDto) {
        Resident resident = housingSystemUtils.extractResident(residentCode);
        House house = housingSystemUtils.extractHouse(advertisementDto.getHouseCode());
        boolean residentOwnsThisHouse = housingSystemUtils.checkResidentOwnsHouse(resident,house);
        if (!residentOwnsThisHouse) {
            throw new ResidentDoesNotOwnTheHouseException("This house does not belong to the calling resident");
        }
        Advertisement advertisement = populateAdvertisementWithDto(advertisementDto);
        Set<Photo> photos = housingSystemUtils.createPhotos(advertisementDto.getPhotoUrls());
        photos.forEach(advertisement::addPhoto);
        resident.addAdvertisement(advertisement);
        advertisement.setHouse(house);
        advertisement.setResident(resident);
        advertisementRepository.save(advertisement);
        residentRepository.save(resident);
        return true;
    }


    /**
     *
     * @param advertisementDto is taken as an input and it is converted to Advertisement
     * @return Advertisement
     */
    private Advertisement populateAdvertisementWithDto(AdvertisementDTO advertisementDto) {
        return Advertisement
                .builder()
                .advertisementState(AdvertisementState.ON_SALE)
                .title(advertisementDto.getTitle())
                .price(advertisementDto.getPrice())
                .description(advertisementDto.getDescription())
                .phone(advertisementDto.getPhone())
                .photos(new HashSet<>())
                .build();
    }


    /**
     *
     * @param residentCode represents resident code that is used to uniquely identify a resident
     * @return Set of AdvertisementDTOs that belong to resident with resident code
     */
    public Set<AdvertisementDTO> getAdvertisementDTOsByResidentCode(long residentCode) {
        Resident resident = housingSystemUtils.extractResident(residentCode);
        Set<Advertisement> advertisements = resident.getAdvertisements();
        return advertisements.stream()
                .map(housingSystemUtils::mapAdvertisementToAdvertisementDto)
                .collect(Collectors.toSet());
    }

    /**
     *
     * @param residentCode represents resident code that is used to uniquely identify a resident
     * @param uuid is used to uniquely identify an advertisement
     * @param advertisementDto new content
     * @return AdvertisementDTO
     */
    @Transactional
    public AdvertisementDTO updateAdvertisement(long residentCode, String uuid, AdvertisementDTO advertisementDto) {
        Resident resident = housingSystemUtils.extractResident(residentCode);
        Advertisement advertisement = housingSystemUtils.extractAdvertisement(uuid);
        boolean residentOwnsThisAdvertisement = housingSystemUtils.checkResidentOwnsAdvertisement(resident, advertisement);
        if (!residentOwnsThisAdvertisement) {
            throw new ResidentDoesNotOwnTheAdvertisementException("Resident does not own this advertisement");
        }

        advertisement.setTitle(advertisementDto.getTitle());
        advertisement.setPrice(advertisementDto.getPrice());
        advertisement.setPhone(advertisementDto.getPhone());
        advertisement.setDescription(advertisementDto.getDescription());
        photoRepository.deleteByAdvertisementId(advertisement.getId());

        Set<Photo> photos = housingSystemUtils.createPhotos(advertisementDto.getPhotoUrls());
        photos.forEach(photoRepository::save);
        photos.forEach(advertisement::addPhoto);
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
        return housingSystemUtils.mapAdvertisementToAdvertisementDto(savedAdvertisement);
    }


    /**
     *
     * @param residentCode represents resident code that is used to uniquely identify a resident
     * @param advertisementUUID is used to uniquely identify an advertisement
     * @return boolean specifies if an advertisement is deleted or not.
     */
    @Transactional
    public boolean deleteAdvertisement(long residentCode, String advertisementUUID) {
        Resident resident = housingSystemUtils.extractResident(residentCode);
        Advertisement advertisement = housingSystemUtils.extractAdvertisement(advertisementUUID);
        boolean residentOwnsThisAdvertisement = housingSystemUtils.checkResidentOwnsAdvertisement(resident, advertisement);
        if (!residentOwnsThisAdvertisement) {
            throw new ResidentDoesNotOwnTheAdvertisementException("resident does not own this advertisement");
        }

        photoRepository.deleteByAdvertisementId(advertisement.getId());

        return advertisementRepository.deleteByUuid(advertisement.getUuid()) > 0;
    }

    /**
     *
     * @param residentCode represents resident code that is used to uniquely identify a resident
     * @param advertisementUUID is used to uniquely identify an advertisement
     * @return AdvertisementDTO
     */
    public AdvertisementDTO getAdvertisementDTOByUUID(long residentCode, String advertisementUUID) {
        Resident resident = housingSystemUtils.extractResident(residentCode);
        Advertisement advertisement = housingSystemUtils.extractAdvertisement(advertisementUUID);
        boolean residentOwnsThisAdvertisement = housingSystemUtils.checkResidentOwnsAdvertisement(resident, advertisement);
        if (!residentOwnsThisAdvertisement){
            throw new ResidentDoesNotOwnTheAdvertisementException("Resident does not own this advertisement");
        }
        return housingSystemUtils.mapAdvertisementToAdvertisementDto(advertisement);
    }
}
