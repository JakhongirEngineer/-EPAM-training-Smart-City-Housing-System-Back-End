package com.epam.smartcityhousingsystem.utils.apiEntities;

/**
 * Home class is used to represent home coming from City Administration module
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class Home {
    private long homeCode;
    private ApiAddress address;

    public Home() {
    }

    public Home(long homeCode, ApiAddress address) {
        this.homeCode = homeCode;
        this.address = address;
    }

    public long getHomeCode() {
        return homeCode;
    }

    public void setHomeCode(long homeCode) {
        this.homeCode = homeCode;
    }

    public ApiAddress getAddress() {
        return address;
    }

    public void setAddress(ApiAddress address) {
        this.address = address;
    }
}
