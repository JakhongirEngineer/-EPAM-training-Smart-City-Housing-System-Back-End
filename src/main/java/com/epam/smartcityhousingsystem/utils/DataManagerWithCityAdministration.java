package com.epam.smartcityhousingsystem.utils;

import com.epam.smartcityhousingsystem.entities.Address;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.entities.enums.Condition;
import com.epam.smartcityhousingsystem.entities.enums.Heating;
import com.epam.smartcityhousingsystem.entities.enums.HouseType;
import com.epam.smartcityhousingsystem.entities.enums.MaterialType;
import com.epam.smartcityhousingsystem.repositories.AddressRepository;
import com.epam.smartcityhousingsystem.repositories.HouseRepository;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.repositories.RoleRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.security.Role;
import com.epam.smartcityhousingsystem.services.HTTPRequestWithHMAC;
import com.epam.smartcityhousingsystem.utils.apiEntities.ApiAddress;
import com.epam.smartcityhousingsystem.utils.apiEntities.FinalResponse;
import com.epam.smartcityhousingsystem.utils.apiEntities.Home;
import com.epam.smartcityhousingsystem.utils.apiEntities.Ownership;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * DataManagerWithCityAdministration class is used to generate House, Resident,Address
 * and populate database with generated values.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@AllArgsConstructor
@Service
public class DataManagerWithCityAdministration {
    private final Random random = new Random();
    private final ResidentRepository residentRepository;
    private final RoleRepository roleRepository;
    private final HouseRepository houseRepository;
    private final AddressRepository addressRepository;
    private final HTTPRequestWithHMAC requestWithHMAC;

    private static final String CITY_MANAGEMENT_OWNERSHIP_PATH = "http://citymanagementbackend-env-1.eba-3swwhqnr.us-east-2.elasticbeanstalk.com/api/v1/request/homesWithOwner";

    /**
     * gets FinalResponse with HMAC token from City Administration module to populate database
     * @return FinalResponse
     */
    public FinalResponse getFinalResponse(){
        return requestWithHMAC.getFinalResponse("get_ownership", CITY_MANAGEMENT_OWNERSHIP_PATH);
    }

    /**
     * populates database with List of Ownerships
     * @param result is extracted from FinalResponse
     */
    public void populateDB(List<Ownership> result) {
        for (int i = 0; i < result.size(); i++) {
            Resident resident = createResident(result.get(i).getCardNumber(), i);
            resident.setHouses(createHouses(result.get(i).getHomes()));

            for (House house : resident.getHouses()) {
                house.setResident(resident);
            }
            residentRepository.save(resident);
        }
    }

    /**
     * creates houses
     * @param homes List of Home
     * @return Set of House
     */
    private Set<House> createHouses(List<Home> homes) {
        Set<House> houses = new HashSet<>();
        for (Home home : homes) {
            House house = createHouse(home);
            houses.add(house);
        }
        return houses;
    }

    /**
     * generates House
     * @param home Home
     * @return House
     */
    private House createHouse(Home home) {
        int randomNumber = random.nextInt(10);
        House house = new House();
        Address address = createAddress(home.getAddress());
        house.setAddress(address);

        house.setCode(home.getHomeCode());
        house.setFurnished( randomNumber > 5);
        house.setHouseType(HouseType.APARTMENT);
        house.setCondition(randomNumber > 0 && randomNumber < 3 ? Condition.NEW : randomNumber > 3 && randomNumber < 7 ? Condition.AVERAGE : Condition.OLD);
        house.setHeating(randomNumber > 6 ? Heating.ELECTRIC : Heating.GAS);
        house.setMaterialType(randomNumber > 6 ? MaterialType.CONCRETE : MaterialType.BRICK);
        house.setCeilingHeight(randomNumber > 5 ? 2.5 : 3);
        house.setNumberOfRooms(random.nextInt(5) + 1);
        house.setTotalArea(house.getNumberOfRooms() * house.getCeilingHeight() * 10);

        address.setHouse(house);
        houseRepository.save(house);
        return house;
    }

    /**
     * generates Address
     * @param apiAddress ApiAddress
     * @return Address
     */
    private Address createAddress(ApiAddress apiAddress) {
        Address address = new Address();
        address.setDistrict(apiAddress.getDistrict());
        address.setHomeNumber(apiAddress.getHomeNumber());
        address.setStreet(apiAddress.getStreet());
        addressRepository.save(address);
        return address;
    }

    /**
     *
     * @param residentCode resident code represents resident uniquely in the database
     * @param index is used to generate random and unique emails, names, and passwords
     * @return Resident
     */
    private Resident createResident(long residentCode, int index) {
        Resident resident = new Resident();
        resident.setResidentCode(residentCode);
        resident.setEmail("resident_"+index + "@gmail.com");
        resident.setEnabled(true);
        resident.setSignedUp(false);
        resident.setFirstName("Resident_" + index);
        resident.setLastName("LastName_" + index);
        resident.setPassword("password_" + index);

        var role = roleRepository.findByName("RESIDENT");
        Set<Role> roles = new HashSet<>();
        roles.add(role.get());
        resident.setRoles(roles);
        resident.setTokenExpired(false);

        return resident;
    }
}
