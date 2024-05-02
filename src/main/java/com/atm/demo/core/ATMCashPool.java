package com.atm.demo.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Getter
public class ATMCashPool {

    private static final Logger logger = Logger.getLogger(ATMCashPool.class.getName());

    ConcurrentSkipListMap<Integer, Integer> cashPool;

    private final ReentrantLock lock = new ReentrantLock();

    public ATMCashPool(List<Cash> cashInput) {
        cashPool = new ConcurrentSkipListMap<>();
        for (Cash atmcash : cashInput) {
            cashPool.put(atmcash.denomination().getValue(), atmcash.quantity());
        }
    }

    /**
     * Process the cashwithdrawal request as per the amount requested.
     * validates the ampunt and denomonations present in the cashpool and balance
     * and denominations not valid throws Exception
     * @param amount
     * @return CashWithdrawRequest
     */
    public CashWithdrawRequest processCashWithdrawalReq(int amount) {
        lock.lock();
        CashWithdrawRequest cashWithdrawRequest = new CashWithdrawRequest();
        try {
            validateAmountAndDenominations(amount);
            List<Integer> availableDenom = getAvailableDenominations();
            if (availableDenom.isEmpty()) {
                throw new RuntimeException("ATM Does not have cash at the moment");
            }
            for (Integer denomination : availableDenom) {
                if (cashPool.get(denomination) != 0) {
                    int maxDenomCount = amount / denomination;
                    int clacDeomCount = Math.min(maxDenomCount, cashPool.get(denomination));
                    amount -= (clacDeomCount * denomination);
                    cashWithdrawRequest.addAmount(denomination, clacDeomCount);
                    logger.info("Denomination withdrawal is : " + denomination + " :: " + clacDeomCount);
                }
            }
            updateCashpoolWithWithdrawalAmountBasedOnDenomination(cashWithdrawRequest);
        } finally {
            lock.unlock();
        }
        return cashWithdrawRequest;
    }

    private void validateAmountAndDenominations(int amount) {
        List<Integer> availableDenom = getAvailableDenominations();
        if (availableDenom.isEmpty()) {
            throw new RuntimeException("There is no money in ATM");
        }
        int balance = cashPool.entrySet().stream().mapToInt(den -> den.getKey() * den.getValue()).sum();
        if (balance < amount) {
            throw new RuntimeException("Balance is low");
        }
    }

    private List<Integer> getAvailableDenominations() {
        return new ArrayList<>(cashPool.descendingKeySet());
    }

    private void updateCashpoolWithWithdrawalAmountBasedOnDenomination(CashWithdrawRequest cashWithdrawRequest) {
        for (Map.Entry<Integer, Integer> cashWithdraw : cashWithdrawRequest.getWithdrawalCashMap().entrySet()) {
            Integer denominationCount = cashPool.get(cashWithdraw.getKey());
            cashPool.put(cashWithdraw.getKey(), denominationCount - cashWithdraw.getValue());
        }
    }
}
