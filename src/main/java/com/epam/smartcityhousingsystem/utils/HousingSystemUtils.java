package com.epam.smartcityhousingsystem.utils;

import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.entities.Photo;
import com.epam.smartcityhousingsystem.exceptions.AdvertisementNotFoundException;
import com.epam.smartcityhousingsystem.exceptions.ResidentNotFoundException;
import com.epam.smartcityhousingsystem.exceptions.HouseNotFoundException;
import com.epam.smartcityhousingsystem.repositories.AdvertisementRepository;
import com.epam.smartcityhousingsystem.repositories.HouseRepository;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * HousingSystemUtils is a utility class that provides utility methods
 * for other service classes.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@RequiredArgsConstructor
@Component
public class HousingSystemUtils {

    private final ResidentRepository residentRepository;
    private final AdvertisementRepository advertisementRepository;
    private final HouseRepository houseRepository;

    /**
     * based on urls, creates Photos
     *
     * @param photoUrls Set of urls
     * @return Set of Photo
     */
    public Set<Photo> createPhotos(Set<String> photoUrls) {
        return photoUrls
                .stream()
                .map(Photo::new).collect(Collectors.toSet());
    }

    /**
     * creates AdvertisementDTO based on Advertisement
     * @param advertisement Advertisement
     * @return AdvertisementDTO
     */
    public AdvertisementDTO mapAdvertisementToAdvertisementDto(Advertisement advertisement) {
        return AdvertisementDTO
                .builder()
                .uuid(advertisement.getUuid())
                .title(advertisement.getTitle())
                .residentCode(advertisement.getResident().getResidentCode())
                .phone(advertisement.getPhone())
                .price(advertisement.getPrice())
                .description(advertisement.getDescription())
                .houseCode(advertisement.getHouse().getCode())
                .photoUrls(
                        advertisement.getPhotos()
                        .stream()
                        .map(Photo::getUrl)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    /**
     * checks if the resident owns the house
     *
     * @param resident Residnet
     * @param house House
     * @return boolean
     */
    public boolean checkResidentOwnsHouse(Resident resident, House house) {
        return resident
                .getHouses()
                .stream()
                .anyMatch(h -> h.getCode()==house.getCode());

    }

    /**
     * checks if the resident owns the advertisement
     *
     * @param resident Resident
     * @param advertisement Advertisement
     * @return boolean
     */
    public boolean checkResidentOwnsAdvertisement(Resident resident, Advertisement advertisement){
        return resident
                .getAdvertisements()
                .stream()
                .anyMatch(ad -> ad.getId()==advertisement.getId());
    }

    /**
     * based on residentCode, finds resident, if not found throws <code>ResidentNotFoundException</code>
     *
     * @param residentCode resident code that uniquely represents a resident in the database
     * @return Resident
     */
    public Resident extractResident(long residentCode) {
        Optional<Resident> residentOptional = residentRepository.findByResidentCode(residentCode);
        return residentOptional
                .orElseThrow(() ->new ResidentNotFoundException("Resident not found"));
    }

    /**
     * based on houseCode, finds house, if not found, throws <code>HouseNotFoundException</code>
     *
     * @param houseCode house code uniquely represents house in the database
     * @return House
     */
    public House extractHouse(long houseCode){
        Optional<House> optionalHouse = houseRepository.findByCode(houseCode);
        return optionalHouse
                .orElseThrow(() -> new HouseNotFoundException("House not found."));
    }

    /**
     * based on advertisementUUID, finds advertisement, if not found, throws <code>AdvertisementNotFoundException</code>
     *
     * @param advertisementUUID is used uniquely identify advertisement
     * @return Advertisement
     */
    public Advertisement extractAdvertisement(String advertisementUUID) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findByUuid(advertisementUUID);
        return  advertisementOptional
                .orElseThrow(() -> new AdvertisementNotFoundException("advertisement not found"));
    }

    /**
     * maps House to HouseDTO
     * @param house House
     * @return HouseDTO
     */
    public HouseDTO mapHouseToHouseDTO(House house) {
        return HouseDTO
                .builder()
                .houseType(house.getHouseType())
                .address(house.getAddress())
                .ceilingHeight(house.getCeilingHeight())
                .code(house.getCode())
                .condition(house.getCondition())
                .furnished(house.isFurnished())
                .heating(house.getHeating())
                .materialType(house.getMaterialType())
                .residentCode(house.getResident().getResidentCode())
                .numberOfRooms(house.getNumberOfRooms())
                .totalArea(house.getTotalArea())
                .build();
    }

    /**
     * maps Resident to ResidentDTO
     * @param resident Resident object
     * @return ResidentDTO
     */
    // mapstruct
    public ResidentDTO mapResidentToResidentDTO(Resident resident){
        return ResidentDTO.builder()
                .residentCode(resident.getResidentCode())
                .email(resident.getEmail())
                .firstName(resident.getFirstName())
                .lastName(resident.getLastName())
                .advertisementUUIDs(
                        resident.getAdvertisements()
                        .stream()
                        .map(advertisement -> advertisement.getUuid())
                        .collect(Collectors.toSet())
                ).houseCodes(
                        resident.getHouses()
                        .stream().map(house -> house.getCode())
                        .collect(Collectors.toSet())
                )
                .build()
                ;
    }
}
