package com.epam.smartcityhousingsystem.utils.apiEntities;

/**
 * ApiAddress class is used to parse address data coming from City Administration module
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

public class ApiAddress {
    private String homeNumber;
    private String district;
    private String street;

    public ApiAddress() {

    }

    public ApiAddress(String homeNumber, String district, String street) {
        this.homeNumber = homeNumber;
        this.district = district;
        this.street = street;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


}
