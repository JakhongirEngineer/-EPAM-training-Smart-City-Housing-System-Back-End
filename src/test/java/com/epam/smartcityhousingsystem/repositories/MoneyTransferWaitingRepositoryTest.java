package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.MoneyTransferWaiting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jakhongir Rasulov on 11/24/21
 * @project smartcity-housing-system
 */

@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class MoneyTransferWaitingRepositoryTest {
    @Autowired
    MoneyTransferWaitingRepository moneyTransferWaitingRepository;

    @BeforeEach
    void setUp() {
        MoneyTransferWaiting moneyTransferWaiting = new MoneyTransferWaiting();
        moneyTransferWaiting.setId(1234L);
        moneyTransferWaiting.setAdvertisementUUID(UUID.randomUUID().toString());
        moneyTransferWaiting.setReceiverCardNumber(12345L);
        moneyTransferWaiting.setSenderCardNumber(54321L);

        moneyTransferWaitingRepository.save(moneyTransferWaiting);
    }

    @Test
    @DisplayName("tests there exists money transfer waiting with the given resident codes")
    void testRetrievingMoneyTransferWaitingWithCorrectCodes(){
        Optional<List<MoneyTransferWaiting>> moneyTransferWaitingOptionals = moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(54321L, 12345L);

        assertAll(
                "tests money transfer waiting",
                ()->{
                    assertTrue(moneyTransferWaitingOptionals.isPresent());
                },
                ()->{
                    assertEquals(54321L, moneyTransferWaitingOptionals.get().get(0).getSenderCardNumber());
                },
                ()->{
                    assertEquals(12345L, moneyTransferWaitingOptionals.get().get(0).getReceiverCardNumber());
                }
        );
    }

    @Test
    @DisplayName("tests there does not exist money transfer waiting with wrong codes")
    void testRetrievingMoneyTransferWaitingWithIncorrectCodes(){
        Optional<List<MoneyTransferWaiting>> moneyTransferWaitingOptional = moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(55555L, 676767L);
        assertTrue(moneyTransferWaitingOptional.get().isEmpty());
    }

    @Test
    @DisplayName("tests there does not exist money transfer waiting with wrong sender code")
    void testMoneyTransferSenderCodeIsIncorrect(){
        Optional<List<MoneyTransferWaiting>> bySenderCardNumberAndReceiverCardNumber = moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(55555L, 12345L);
        assertTrue(bySenderCardNumberAndReceiverCardNumber.get().isEmpty());
    }

    @Test
    @DisplayName("tests there does not exist money transfer waiting with wrong receiver code")
    void testMoneyTransferReceiverCodeIsIncorrect(){
        Optional<List<MoneyTransferWaiting>> bySenderCardNumberAndReceiverCardNumber = moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(54321L, 55555L);
        assertTrue(bySenderCardNumberAndReceiverCardNumber.get().isEmpty());
    }
}