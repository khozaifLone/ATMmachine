package com.atm.demo.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Getter
public class ATMMachine {

    private final static Logger logger = Logger.getLogger(ATMMachine.class.getName());

    private final ATMCashPool atmCashPool;

    private Map<String, CashWithdrawRequest> transactionsOnHold;

    /**
     * Initialize the ATM Machine with cash input.
     * @param cashInput
     */
    public ATMMachine(List<Cash> cashInput) {
        atmCashPool = new ATMCashPool(cashInput);
        transactionsOnHold = new HashMap<>();
    }

    /**
     * Withdraw cash based on the amount provided
     * @param amount
     * validates if the amount is valid based on our assumptions
     */
    public void withDrawCash(Integer amount) {
        if (amount <= 0 || amount % 10 != 0) {
            throw new RuntimeException("Invalid amount");
        }
        CashWithdrawRequest cashWithdrawRequest = atmCashPool.processCashWithdrawalReq(amount);
        transactionsOnHold.put(Thread.currentThread().getName(), cashWithdrawRequest);
    }
}
