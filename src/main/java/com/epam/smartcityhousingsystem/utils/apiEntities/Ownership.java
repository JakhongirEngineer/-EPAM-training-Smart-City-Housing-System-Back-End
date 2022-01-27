package com.epam.smartcityhousingsystem.utils.apiEntities;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Ownership class is used to represent ownership coming from City Administration module
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class Ownership {
    private long cardNumber;
    private List<Home> homes;

    public Ownership() {
    }

    public Ownership(long cardNumber, List<Home> homes) {
        this.cardNumber = cardNumber;
        this.homes = homes;
    }

    private void addHome(Home home) {
        this.homes.add(home);
    }

    private boolean removeHomeByHomeCode(long homeCode) {
        this.homes = this.homes
                .stream()
                .filter(home -> home.getHomeCode() != homeCode)
                .collect(Collectors.toList());
        return true;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }
}
