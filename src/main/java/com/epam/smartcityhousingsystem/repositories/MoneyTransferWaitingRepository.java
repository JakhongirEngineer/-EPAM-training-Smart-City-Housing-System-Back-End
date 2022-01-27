package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.MoneyTransferWaiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Repository
public interface MoneyTransferWaitingRepository extends JpaRepository<MoneyTransferWaiting,Long> {

    Optional<List<MoneyTransferWaiting>> findBySenderCardNumberAndReceiverCardNumber(Long senderCardNumber, Long receiverCardNumber);
}
